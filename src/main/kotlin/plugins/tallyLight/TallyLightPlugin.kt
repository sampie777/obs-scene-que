package plugins.tallyLight


import GUI
import com.google.gson.Gson
import gui.Refreshable
import objects.OBSClient
import objects.OBSState
import plugins.tallyLight.websocketEvents.SceneItemVisibilityChanged
import plugins.tallyLight.websocketEvents.SourceCreated
import plugins.tallyLight.websocketEvents.SourceDestroyed
import plugins.tallyLight.websocketEvents.SourceRenamed
import java.util.logging.Logger

class TallyLightPlugin : Refreshable {
    private val logger = Logger.getLogger(TallyLightPlugin::class.java.name)

    companion object {
        val tallies: ArrayList<TallyLight> = arrayListOf(
            TallyLight("Camera 1", "192.168.178.143"),
            TallyLight("Camera 2", ""),
            TallyLight("Camera 3", ""),
            TallyLight("Camera 4", ""),
            TallyLight("Camera 5", "")
        )
    }

    fun enable() {
        sendUpdates()

        GUI.register(this)

        OBSClient.preregisterCallback("TallyLightCallbacks") { controller ->
            controller.registerEventCallback("SceneItemVisibilityChanged") { payload ->
                val event = Gson().fromJson(payload, SceneItemVisibilityChanged::class.java)
                sceneItemVisibilityChanged(event)
            }
            controller.registerEventCallback("SourceDestroyed") { payload ->
                val event = Gson().fromJson(payload, SourceDestroyed::class.java)
                sourceDestroyedEvent(event)
            }
            controller.registerEventCallback("SourceCreated") { payload ->
                println(payload)
                val event = Gson().fromJson(payload, SourceCreated::class.java)
                sourceCreatedEvent(event)
            }
            controller.registerEventCallback("SourceRenamed") { payload ->
                println(payload)
                val event = Gson().fromJson(payload, SourceRenamed::class.java)
                sourceRenamedEvent(event)
            }
        }
    }

    fun disable() {
        GUI.unregister(this)

        tallies.forEach { it.isLive = false }
        sendUpdates()
    }

    private fun sceneItemVisibilityChanged(event: SceneItemVisibilityChanged) {
        logger.info("Received SceneItemVisibilityChanged for scene: ${event.sceneName}")

        val tallyLight = tallies.find { it.cameraSourceName == event.itemName }
        if (tallyLight == null) {
            logger.info("No tally light found for: ${event.itemName}")
            return
        }

        tallyLight.isLive = event.itemVisible ?: false
        tallyLight.update()
    }

    private fun sourceDestroyedEvent(event: SourceDestroyed) {
        logger.info("Received SourceDestroyed event")

        val tallyLight = tallies.find { it.cameraSourceName == event.sourceName } ?: return

        logger.info("Setting Tally Light live status to false, because it has no source in OBS")
        tallyLight.isLive = false
        tallyLight.update()
    }

    private fun sourceCreatedEvent(event: SourceCreated) {
        logger.info("Received SourceCreated event")

        val tallyLight = tallies.find { it.cameraSourceName == event.sourceName } ?: return

        logger.info("Setting Tally Light live status to true, because it has just been created in OBS")
        tallyLight.isLive = true
        tallyLight.update()
    }

    private fun sourceRenamedEvent(event: SourceRenamed) {
        logger.info("Received SourceRenamed event")

        val tallyLight = tallies.find { it.cameraSourceName == event.previousName } ?: return

        logger.info("Renaming Tally Light source name from '${event.previousName}' to '${event.newName}'")
        tallyLight.cameraSourceName = event.newName ?: return
    }

    fun sendUpdates() {
        logger.fine("Sending tally light updates")
        tallies.forEach { it.update() }
    }

    override fun refreshScenes() {
        OBSState.scenes.forEach { scene ->
            scene.sources.forEach { source ->
                tallies.find { it.cameraSourceName == source.name }?.isLive = source.isVisible
            }
        }

        sendUpdates()
    }
}
