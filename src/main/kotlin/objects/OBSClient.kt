package objects

import GUI
import config.Config
import net.twasi.obsremotejava.OBSRemoteController
import net.twasi.obsremotejava.events.responses.SwitchScenesResponse
import net.twasi.obsremotejava.objects.Scene
import net.twasi.obsremotejava.objects.Source
import net.twasi.obsremotejava.requests.GetCurrentScene.GetCurrentSceneResponse
import net.twasi.obsremotejava.requests.GetSceneList.GetSceneListResponse
import net.twasi.obsremotejava.requests.ResponseBase
import objects.notifications.Notifications
import java.util.*
import java.util.logging.Logger

object OBSClient {
    private var logger = Logger.getLogger(OBSClient::class.java.name)

    private var controller: OBSRemoteController? = null
    private var reconnecting: Boolean = false

    fun start() {
        logger.info("Connecting to OBS on: ${Config.obsAddress}")
        Globals.OBSConnectionStatus = if (!reconnecting) OBSStatus.CONNECTING else OBSStatus.RECONNECTING
        GUI.refreshOBSStatus()

        controller = OBSRemoteController(Config.obsAddress, false)

        if (controller!!.isFailed) { // Awaits response from OBS
            logger.severe("Failed to create controller")
            processFailedConnection("Could not connect to OBS", reconnect = true)
        }

        registerCallbacks()

        try {
            controller!!.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun processFailedConnection(message: String, reconnect: Boolean = true) {
        Globals.OBSConnectionStatus = OBSStatus.CONNECTION_FAILED
        GUI.refreshOBSStatus()

        if (!reconnecting) {
            Notifications.add(message, "OBS")
        }

        if (reconnect) {
            startReconnectingTimeout()
        }
    }

    private fun startReconnectingTimeout() {
        val connectionRetryTimer = Timer()
        connectionRetryTimer.schedule(object : TimerTask() {
            override fun run() {
                reconnecting = true
                start()
            }
        }, Config.obsReconnectionTimeout)
    }

    private fun registerCallbacks() {
        try {
            controller!!.registerDisconnectCallback {
                logger.info("Disconnected from OBS")
                Globals.OBSConnectionStatus = OBSStatus.DISCONNECTED
                GUI.refreshOBSStatus()

                Notifications.add("Disconnected from OBS", "OBS")

                startReconnectingTimeout()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerDisconnectCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register disconnect callback: cannot notify when connection is lost",
                "OBS"
            )
        }

        try {
            controller!!.registerConnectCallback {
                logger.info("Connected to OBS")
                Globals.OBSConnectionStatus = OBSStatus.CONNECTED
                GUI.refreshOBSStatus()

                if (reconnecting) {
                    Notifications.add("Connection re-established", "OBS")
                }
                reconnecting = false

                getScenes()

                getCurrentSceneFromOBS()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerConnectCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register connect callback: scenes cannot be loaded at startup",
                "OBS"
            )
        }

        try {
            controller!!.registerScenesChangedCallback {
                logger.fine("Processing scenes changed event")
                getScenes()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerScenesChangedCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register scenesChanged callback: new scenes cannot be loaded",
                "OBS"
            )
        }

        try {
            controller!!.registerSwitchScenesCallback { responseBase: ResponseBase ->
                logger.fine("Processing scene switch event")
                val response = responseBase as SwitchScenesResponse

                processNewScene(response.sceneName)
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerSwitchScenesCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register switchScenes callback: cannot detect scene changes",
                "OBS"
            )
        }
    }

    private fun getScenes() {
        logger.info("Retrieving scenes")
        Globals.OBSActivityStatus = OBSStatus.LOADING_SCENES
        GUI.refreshOBSStatus()

        controller!!.getScenes { response: ResponseBase ->
            val res = response as GetSceneListResponse
            logger.info(res.scenes.size.toString() + " scenes retrieved")

            setOBSScenes(res.scenes)
        }
    }

    fun setOBSScenes(scenes: List<Scene>) {
        Globals.scenes.clear()
        for (scene in scenes) {
            val tScene = responseSceneToTScene(scene.name, scene.sources)

            Globals.scenes.add(tScene)
        }

        GUI.refreshScenes()
        Globals.OBSActivityStatus = null
        GUI.refreshOBSStatus()
    }

    fun responseSceneToTScene(name: String?, sources: List<Source>?): TScene {
        val tScene = TScene(name)

        if (sources != null) {
            val tSources = sources.map { source: Source -> TSource(source.name, source.type) }
            tScene.sources = tSources
        }
        return tScene
    }

    fun setActiveScene(scene: TScene) {
        logger.info("Setting new current scene to: $scene")
        controller!!.setCurrentScene(scene.name) { }
    }

    private fun setPreviewScene(scene: TScene) {
        logger.info("Setting new preview scene to: $scene")
        controller!!.setPreviewScene(scene.name) { }
    }

    /**
     * Actively request the current scene from BOS
     */
    private fun getCurrentSceneFromOBS() {
        logger.fine("Retrieving current scene")
        controller!!.getCurrentScene { res: ResponseBase ->
            val currentScene = res as GetCurrentSceneResponse

            processNewScene(currentScene.name)
        }
    }

    /**
     * Set the new scene name as new current scene and notify everyone of this change
     */
    fun processNewScene(sceneName: String) {
        Globals.activeOBSSceneName = sceneName

        logger.info("New scene: " + Globals.activeOBSSceneName)

        GUI.switchedScenes()
        setPreviewScene(Que.previewNext() ?: return)
    }
}
