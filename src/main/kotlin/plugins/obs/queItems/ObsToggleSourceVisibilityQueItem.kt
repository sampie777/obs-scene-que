package plugins.obs.queItems

import net.twasi.obsremotejava.requests.GetCurrentScene.GetCurrentSceneResponse
import net.twasi.obsremotejava.requests.GetSceneItemProperties.GetSceneItemPropertiesResponse
import net.twasi.obsremotejava.requests.ResponseBase
import objects.OBSClient
import objects.OBSState
import objects.TScene
import objects.que.JsonQue
import objects.que.QueItem
import plugins.obs.ObsPlugin
import java.awt.Color
import java.util.logging.Logger

enum class SourceVisibilityState(val text: String) {
    TOGGLE("Toggle"),
    SHOW("Show"),
    HIDE("Hide")
}

class ObsToggleSourceVisibilityQueItem(
    override val plugin: ObsPlugin,
    private val sceneName: String,
    private val sourceName: String,
    private val targetState: SourceVisibilityState = SourceVisibilityState.TOGGLE
) : QueItem {

    private val logger = Logger.getLogger(ObsToggleSourceVisibilityQueItem::class.java.name)

    companion object {
        const val CURRENT_SCENE_NAME = "-- current --"

        fun fromJson(plugin: ObsPlugin, jsonQueItem: JsonQue.QueItem) : QueItem {
            val sceneName = jsonQueItem.data["sceneName"]!!
            val sourceName = jsonQueItem.data["sourceName"]!!
            val targetState = SourceVisibilityState.valueOf(jsonQueItem.data["targetState"]!!)
            return ObsToggleSourceVisibilityQueItem(plugin, sceneName, sourceName, targetState)
        }
    }

    override val name: String = renderText()
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    override fun renderText(): String {
        if (targetState == SourceVisibilityState.TOGGLE) {
            return "Toggle source: $sourceName (${sceneName})"
        }
        if (targetState == SourceVisibilityState.SHOW) {
            return "Show source: $sourceName (${sceneName})"
        }
        return "hide source: $sourceName (${sceneName})"
    }

    override fun toString() = renderText()

    override fun toJson(): JsonQue.QueItem {
        val jsonQueItem = super.toJson()
        jsonQueItem.data["sceneName"] = sceneName
        jsonQueItem.data["sourceName"] = sourceName
        jsonQueItem.data["targetState"] = targetState.toString()
        return jsonQueItem
    }

    override fun activate() {
        if (sceneName == CURRENT_SCENE_NAME) {
            activateForCurrentScene()
        } else {
            activateForSpecificScene(sceneName)
        }
    }

    private fun activateForSpecificScene(sceneName: String) {
        if (targetState == SourceVisibilityState.TOGGLE) {
            OBSClient.getController()!!.getSceneItemProperties(sceneName, sourceName) { res: ResponseBase ->
                val source = res as GetSceneItemPropertiesResponse

                setSourceVisibility(sceneName, !source.visible)
            }
        } else {
            setSourceVisibility(sceneName, targetState == SourceVisibilityState.SHOW)
        }
    }

    private fun activateForCurrentScene() {
        OBSClient.getController()!!.getCurrentScene { res: ResponseBase ->
            val currentScene = res as GetCurrentSceneResponse

            if (targetState == SourceVisibilityState.TOGGLE) {
                val source = currentScene.sources.find { it.name == sourceName }
                if (source == null) {
                    logger.info("Current scene ${currentScene.name} doesn't contain source $sourceName")
                    return@getCurrentScene
                }

                setSourceVisibility(currentScene.name, !source.isRender)
            } else {
                setSourceVisibility(currentScene.name, targetState == SourceVisibilityState.SHOW)
            }
        }
    }

    private fun setSourceVisibility(sceneName: String, visible: Boolean) {
        OBSClient.getController()!!.setSourceVisibility(
            sceneName,
            sourceName,
            visible
        ) {
            if (OBSState.currentSceneName == sceneName) {
                reactivateScene(sceneName)
            }
        }
    }

    private fun reactivateScene(sceneName: String) {
        OBSClient.setActiveScene(TScene(sceneName))
    }
}