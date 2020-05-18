package plugins.obs

import objects.OBSClient
import objects.OBSState
import objects.TScene
import objects.que.Que
import plugins.common.BasePlugin
import plugins.common.QueItem
import themes.Theme
import javax.swing.JLabel

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

    override fun getListCellRendererComponent(
        cell: JLabel,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ) {
        super.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)

        val sceneExist = OBSState.scenes.find { it.name == scene.name }

        if (scene.name == Que.current()?.name && index == Que.currentIndex() && OBSState.currentSceneName == scene.name) {
            cell.background = if (isSelected) Theme.get.ACTIVE_QUE_AND_OBS_SELECTED_COLOR
            else Theme.get.ACTIVE_QUE_AND_OBS_COLOR

        } else if (scene.name == Que.current()?.name && index == Que.currentIndex()) {
            cell.background = if (isSelected) Theme.get.ACTIVE_QUE_SELECTED_COLOR
            else Theme.get.ACTIVE_QUE_COLOR

        } else if (OBSState.currentSceneName == scene.name) {
            cell.background = if (isSelected) Theme.get.ACTIVE_OBS_SELECTED_COLOR
            else Theme.get.ACTIVE_OBS_COLOR

        } else if (sceneExist == null) {
            cell.background = if (isSelected) Theme.get.NON_EXISTING_SELECTED_COLOR
            else Theme.get.NON_EXISTING_COLOR
        }
    }
}