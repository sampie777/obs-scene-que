package plugins.obs

import objects.OBSClient
import objects.TScene
import plugins.common.BasePlugin
import plugins.common.QueItem

class ObsSceneQueItem(override val plugin: BasePlugin, val scene: TScene) : QueItem {
    override val name: String = scene.name

    override fun activate() {
        OBSClient.setActiveScene(scene)
    }

    override fun deactivate() {}

    override fun toConfigString(): String {
        return scene.name
    }

    override fun toString(): String = name
}