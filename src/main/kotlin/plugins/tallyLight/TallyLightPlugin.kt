package plugins.tallyLight


import GUI
import com.google.gson.Gson
import gui.Refreshable
import objects.OBSClient
import objects.OBSState
import objects.notifications.Notifications
import plugins.tallyLight.filters.RehobothFilter
import plugins.tallyLight.filters.TallyLightFilterInterface
import plugins.tallyLight.websocketEvents.SceneItemVisibilityChanged
import plugins.tallyLight.websocketEvents.SourceCreated
import plugins.tallyLight.websocketEvents.SourceDestroyed
import plugins.tallyLight.websocketEvents.SourceRenamed
import java.util.logging.Logger

object TallyLightPlugin : Refreshable {
    private val logger = Logger.getLogger(TallyLightPlugin::class.java.name)

    val tallies: ArrayList<TallyLight> = arrayListOf(
        TallyLight("Camera 1", "192.168.178.68"),
        TallyLight("Camera 2", "192.168.178.90"),
        TallyLight("Camera 3", "192.168.178.143"),
        TallyLight("Camera 4", ""),
        TallyLight("Camera 5", "")
    )

    val filters: ArrayList<TallyLightFilterInterface> = arrayListOf(
        RehobothFilter()
    )

    private var isEnabled: Boolean = false

    fun enable() {
        isEnabled = true

        TallyLightProperties.load()

        sendUpdates()

        GUI.register(this)

        registerOBSCallbacks()
    }

    private fun registerOBSCallbacks() {
        OBSClient.preregisterCallback(TallyLightPlugin::class.java.name) { controller ->
            controller.registerEventCallback("SceneItemVisibilityChanged") { payload ->
                if (!isEnabled) return@registerEventCallback
                val event = Gson().fromJson(payload, SceneItemVisibilityChanged::class.java)
                sceneItemVisibilityChangedEvent(event)
            }

            controller.registerEventCallback("SourceDestroyed") { payload ->
                if (!isEnabled) return@registerEventCallback
                val event = Gson().fromJson(payload, SourceDestroyed::class.java)
                sourceDestroyedEvent(event)
            }

            controller.registerEventCallback("SourceCreated") { payload ->
                if (!isEnabled) return@registerEventCallback
                val event = Gson().fromJson(payload, SourceCreated::class.java)
                sourceCreatedEvent(event)
            }

            controller.registerEventCallback("SourceRenamed") { payload ->
                if (!isEnabled) return@registerEventCallback
                val event = Gson().fromJson(payload, SourceRenamed::class.java)
                sourceRenamedEvent(event)
            }
        }
    }

    fun disable() {
        isEnabled = false

        GUI.unregister(this)

        TallyLightProperties.save()

        tallies.forEach { it.isLive = false }
        sendUpdates(async = false)
    }

    private fun sceneItemVisibilityChangedEvent(event: SceneItemVisibilityChanged) {
        logger.info("Received SceneItemVisibilityChanged for scene: ${event.sceneName}")
        changeTallyWithSourceNameLiveStatus(event.itemName, event.itemVisible ?: false)
    }

    private fun sourceDestroyedEvent(event: SourceDestroyed) {
        logger.info("Received SourceDestroyed event")
        changeTallyWithSourceNameLiveStatus(event.sourceName, false)
    }

    private fun sourceCreatedEvent(event: SourceCreated) {
        logger.info("Received SourceCreated event")
        changeTallyWithSourceNameLiveStatus(event.sourceName, true)
    }

    private fun sourceRenamedEvent(event: SourceRenamed) {
        logger.info("Received SourceRenamed event")

        val tallyLight = tallies.find { it.cameraSourceName == event.previousName } ?: return

        logger.info("Renaming Tally Light source name from '${event.previousName}' to '${event.newName}'")
        tallyLight.cameraSourceName = event.newName ?: return
    }

    private fun changeTallyWithSourceNameLiveStatus(sourceName: String?, isLive: Boolean) {
        val tallyLight = tallies.find { it.cameraSourceName == sourceName } ?: return

        logger.info("Setting Tally Light live status to $isLive")
        tallyLight.isLive = isLive
        tallyLight.update()

        applyFilters()
    }

    private fun sendUpdates(async: Boolean = true) {
        logger.fine("Sending tally light updates")
        tallies.forEach { it.update(async) }

        applyFilters()
    }

    override fun refreshScenes() {
        OBSState.scenes.forEach { scene ->
            scene.sources.forEach { source ->
                tallies.find { it.cameraSourceName == source.name }?.isLive = source.isVisible
            }
        }

        sendUpdates()
    }

    fun enableWriteToFile(writeToFile: Boolean) {
        TallyLightProperties.writeToFile = writeToFile
    }

    private fun applyFilters() {
        filters.forEach {
            if (!it.isEnabled) return@forEach

            try {
                it.apply(tallies)
            } catch (t: Throwable) {
                logger.severe("Failed to apply filter ${it::class.java.name}")
                t.printStackTrace()
                Notifications.add("Failed to apply TallyLight filter ${it::class.java.name}: ${t.localizedMessage}")
            }
        }
    }
}
