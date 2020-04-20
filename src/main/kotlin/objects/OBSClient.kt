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
import java.util.logging.Logger

object OBSClient {
    private var logger = Logger.getLogger(OBSClient::class.java.name)

    private var controller: OBSRemoteController? = null

    fun start() {
        logger.info("Connecting to OBS on: ${Config.obsAddress}")
        Globals.OBSConnectionStatus = OBSStatus.CONNECTING
        GUI.refreshOBSStatus()

        controller = OBSRemoteController(Config.obsAddress, false)

        registerCallbacks()

        try {
            controller!!.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun registerCallbacks() {
        if (controller!!.isFailed) { // Awaits response from OBS
            // Here you can handle a failed connection request
            logger.severe("Failed to create controller")
            Globals.OBSConnectionStatus = OBSStatus.CONNECTION_FAILED
            GUI.refreshOBSStatus()
        }

        controller!!.registerDisconnectCallback {
            logger.info("Disconnected from OBS")
            Globals.OBSConnectionStatus = OBSStatus.DISCONNECTED
            GUI.refreshOBSStatus()
        }

        controller!!.registerConnectCallback {
            logger.info("Connected to OBS")
            Globals.OBSConnectionStatus = OBSStatus.CONNECTED
            GUI.refreshOBSStatus()

            getScenes()

            getCurrentSceneFromOBS()
        }

        controller!!.registerScenesChangedCallback {
            logger.fine("Processing scenes changed event")
            getScenes()
        }

        controller!!.registerSwitchScenesCallback { response: ResponseBase? ->
            logger.fine("Processing scene switch event")
            val sceneResponse = response as SwitchScenesResponse

            processNewScene(sceneResponse.sceneName)
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

    private fun setOBSScenes(scenes: List<Scene>) {
        Globals.scenes.clear()
        for (scene in scenes) {
            val tScene = responseSceneToTScene(scene.name, scene.sources)

            Globals.scenes.add(tScene)
        }

        GUI.refreshScenes()
        Globals.OBSActivityStatus = null
        GUI.refreshOBSStatus()
    }

    private fun responseSceneToTScene(name: String, sources: List<Source>?): TScene {
        val tScene = TScene()
        tScene.name = name

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
    private fun processNewScene(sceneName: String) {
        Globals.activeOBSSceneName = sceneName

        logger.info("New scene: " + Globals.activeOBSSceneName)

        GUI.switchedScenes()
        setPreviewScene(Que.previewNext() ?: return)
    }
}
