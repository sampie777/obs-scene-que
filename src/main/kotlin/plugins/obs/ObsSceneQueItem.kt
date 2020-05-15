package plugins.obs

import objects.OBSClient
import objects.TScene
import plugins.common.QueItem

class ObsSceneQueItem(val scene: TScene) : QueItem {
    override val name: String = scene.name
    override val pluginName: String = "ObsPlugin"

    override fun activate() {
        OBSClient.setActiveScene(scene)
    }

    override fun deactivate() {}

    override fun toConfigString(): String {
        return scene.name
    }

    override fun toString(): String = name
}