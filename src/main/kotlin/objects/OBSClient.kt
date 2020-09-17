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
import objects.que.Que
import plugins.obs.queItems.ObsSceneQueItem
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap

object OBSClient {
    private var logger = Logger.getLogger(OBSClient::class.java.name)

    private var controller: OBSRemoteController? = null
    fun getController() = controller
    private var reconnecting: Boolean = false
    private var isRunning: Boolean = false
    fun isRunning() = isRunning

    private val preregisteredCallbacks = HashMap<String, (controller: OBSRemoteController) -> Unit>()

    fun start() {
        logger.info("Connecting to OBS on: ${Config.obsAddress}")
        OBSState.connectionStatus = if (!reconnecting) OBSClientStatus.CONNECTING else OBSClientStatus.RECONNECTING
        GUI.refreshOBSStatus()

        val obsPassword: String? = if (Config.obsPassword.isEmpty()) null else Config.obsPassword

        controller = OBSRemoteController(Config.obsAddress, false, obsPassword)

        // Await response from OBS
        if (controller!!.isFailed) {
            logger.severe("Failed to create controller")
            processFailedConnection("Could not connect to OBS", reconnect = true)
        }

        registerCallbacks()
    }

    fun stop() {
        logger.info("Disconnecting with OBS")
        controller?.disconnect()

        isRunning = false
    }

    private fun processFailedConnection(message: String, reconnect: Boolean = true) {
        OBSState.connectionStatus = OBSClientStatus.CONNECTION_FAILED
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

    fun preregisterCallback(callbackId: String, callback: (controller: OBSRemoteController) -> Unit) {
        if (preregisteredCallbacks.containsKey(callbackId)) {
            logger.warning("Callback ID '$callbackId' already exist in preregistered callbacks. This will be overwritten.")
        }
        preregisteredCallbacks[callbackId] = callback
    }

    fun clearPreregisteredCallbacks() {
        preregisteredCallbacks.clear()
    }

    private fun registerCallbacks() {
        try {
            controller!!.registerOnError { message, throwable ->
                logger.severe("OBS Controller gave an error: $message")
                throwable.printStackTrace()

                Notifications.add("OBS Connection module gave an unexpected error: $message", "OBS")
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerOnError")
            t.printStackTrace()
            Notifications.add(
                "Failed to register error callback: cannot notify when unexpected errors occur (${t.localizedMessage})",
                "OBS"
            )
        }

        try {
            controller!!.registerDisconnectCallback {
                logger.info("Disconnected from OBS")
                OBSState.connectionStatus = OBSClientStatus.DISCONNECTED
                GUI.refreshOBSStatus()

                Notifications.add("Disconnected from OBS", "OBS")

                startReconnectingTimeout()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerDisconnectCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register disconnect callback: cannot notify when connection is lost (${t.localizedMessage})",
                "OBS"
            )
        }

        try {
            controller!!.registerConnectCallback {
                logger.info("Connected to OBS")
                OBSState.connectionStatus = OBSClientStatus.CONNECTED
                GUI.refreshOBSStatus()

                if (reconnecting) {
                    Notifications.add("Connection re-established", "OBS", markAsRead = true)
                }
                reconnecting = false

                loadScenes()

                getCurrentSceneFromOBS()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerConnectCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register connect callback: scenes cannot be loaded at startup (${t.localizedMessage})",
                "OBS"
            )
        }

        try {
            controller!!.registerConnectionFailedCallback { message: String ->
                logger.severe("Failed to connect to OBS: $message")
                OBSState.connectionStatus = OBSClientStatus.CONNECTION_FAILED
                Notifications.add(
                    "Failed to connect to OBS: $message",
                    "OBS"
                )

                GUI.refreshOBSStatus()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerConnectionFailedCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register connectionFailed callback: connection failures won't be shown (${t.localizedMessage})",
                "OBS"
            )
        }

        try {
            controller!!.registerScenesChangedCallback {
                logger.fine("Processing scenes changed event")
                loadScenes()
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerScenesChangedCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register scenesChanged callback: new scenes cannot be loaded (${t.localizedMessage})",
                "OBS"
            )
        }

        try {
            controller!!.registerSwitchScenesCallback { responseBase: ResponseBase ->
                logger.fine("Processing scene switch event")
                val response = responseBase as SwitchScenesResponse

                try {
                    processNewScene(response.sceneName)
                } catch (t: Throwable) {
                    logger.severe("Could not process new scene change")
                    t.printStackTrace()
                    Notifications.add("Could not process new scene change: ${t.localizedMessage}", "OBS")
                }
            }
        } catch (t: Throwable) {
            logger.severe("Failed to create OBS callback: registerSwitchScenesCallback")
            t.printStackTrace()
            Notifications.add(
                "Failed to register switchScenes callback: cannot detect scene changes (${t.localizedMessage})",
                "OBS"
            )
        }

        registerPreregisteredCallbacks()
    }

    fun registerPreregisteredCallbacks() {
        preregisteredCallbacks.forEach { (callbackId, callback) ->
            try {
                callback.invoke(controller!!)
            } catch (t: Throwable) {
                logger.severe("Failed to register preregistered OBS callback: $callbackId")
                t.printStackTrace()
                Notifications.add(
                    "Failed to register a callback '$callbackId': ${t.localizedMessage}",
                    "OBS"
                )
            }
        }
    }

    /**
     * Actively request the current scene from BOS
     */
    private fun getCurrentSceneFromOBS() {
        logger.fine("Retrieving current scene")
        controller!!.getCurrentScene { res: ResponseBase ->
            val currentScene = try {
                res as GetCurrentSceneResponse
            } catch (t: Throwable) {
                logger.severe("Could not cast response to GetCurrentSceneResponse")
                t.printStackTrace()
                Notifications.add("Could not process 'GetCurrentSceneResponse' from OBS: ${t.localizedMessage}", "OBS")
                return@getCurrentScene
            }

            try {
                processNewScene(currentScene.name)
            } catch (t: Throwable) {
                logger.severe("Could not process current scene")
                t.printStackTrace()
                Notifications.add("Could not process current scene: ${t.localizedMessage}", "OBS")
            }
        }
    }

    /**
     * Set the new scene name as new current scene and notify everyone of this change
     */
    fun processNewScene(sceneName: String) {
        logger.info("New scene: $sceneName")
        OBSState.currentSceneName = sceneName

        GUI.switchedScenes()
        setPreviewScene(getNextQueScene()?.scene ?: return)
    }

    private fun loadScenes() {
        logger.info("Retrieving scenes")
        OBSState.clientActivityStatus = OBSClientStatus.LOADING_SCENES
        GUI.refreshOBSStatus()

        try {
            controller!!.getScenes { response: ResponseBase ->
                val res = try {
                    response as GetSceneListResponse
                } catch (t: Throwable) {
                    logger.severe("Could not cast response to GetSceneListResponse")
                    t.printStackTrace()
                    Notifications.add("Could not process 'GetSceneListResponse' from OBS: ${t.localizedMessage}", "OBS")
                    return@getScenes
                }
                logger.info("${res.scenes.size} scenes retrieved")

                try {
                    processOBSScenesToOBSStateScenes(res.scenes)
                } catch (t: Throwable) {
                    logger.severe("Failed to process scenes")
                    t.printStackTrace()
                    Notifications.add("Something went wrong during scenes processing: ${t.localizedMessage}", "OBS")
                }
            }
        } catch (t: Throwable) {
            logger.severe("Failed to retrieve scenes")
            t.printStackTrace()
            Notifications.add("Something went wrong during retrieving scenes: ${t.localizedMessage}", "OBS")
        }
    }

    fun processOBSScenesToOBSStateScenes(scenes: List<Scene>) {
        logger.info("Set the OBS Scenes")
        OBSState.scenes.clear()
        for (scene in scenes) {
            val tScene = responseSceneToTScene(scene.name, scene.sources)

            OBSState.scenes.add(tScene)
        }

        GUI.refreshScenes()
        OBSState.clientActivityStatus = null
        GUI.refreshOBSStatus()
    }

    fun responseSceneToTScene(name: String?, sources: List<Source>?): TScene {
        val tScene = TScene(name)

        if (sources != null) {
            val tSources = sources.map { source: Source ->
                TSource(source.name, source.type).also {
                    it.isVisible = source.isRender
                }
            }
            tScene.sources = tSources
        }
        return tScene
    }

    fun setActiveScene(scene: TScene) {
        logger.info("Setting new current scene to: $scene")
        controller!!.setCurrentScene(scene.name) {
            GUI.switchedScenes()
            setPreviewScene(getNextQueScene()?.scene ?: return@setCurrentScene)
        }
    }

    private fun setPreviewScene(scene: TScene) {
        logger.info("Setting new preview scene to: $scene")
        controller!!.setPreviewScene(scene.name) { }
    }

    private fun getNextQueScene(): ObsSceneQueItem? {
        for (i in (Que.currentIndex() + 1)..Que.getList().size) {
            if (Que.getAt(i) is ObsSceneQueItem) {
                return Que.getAt(i) as ObsSceneQueItem
            }
        }
        return null
    }
}
