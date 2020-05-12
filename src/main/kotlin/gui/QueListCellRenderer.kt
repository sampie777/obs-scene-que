package gui

import objects.OBSState
import objects.Que
import objects.TScene
import themes.Theme
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.border.EmptyBorder

class QueListCellRenderer : DefaultListCellRenderer() {

    val selectedColor = Theme.get.SELECTED_COLOR
    val activeOBSColor = Theme.get.ACTIVE_OBS_COLOR
    val activeOBSSelectedColor = Theme.get.ACTIVE_OBS_SELECTED_COLOR
    val activeQueColor = Theme.get.ACTIVE_QUE_COLOR
    val activeQueSelectedColor = Theme.get.ACTIVE_QUE_SELECTED_COLOR
    val activeQueAndOBSColor = Theme.get.ACTIVE_QUE_AND_OBS_COLOR
    val activeQueAndOBSSelectedColor = Theme.get.ACTIVE_QUE_AND_OBS_SELECTED_COLOR
    val nonExistingColor = Theme.get.NON_EXISTING_COLOR
    val nonExistingSelectedColor = Theme.get.NON_EXISTING_SELECTED_COLOR

    override fun getListCellRendererComponent(
        list: JList<*>,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
        cell.border = EmptyBorder(1, 10, 1, 10)

        if (value == null) {
            return cell
        }

        val scene = value as TScene
        val sceneExist = OBSState.scenes.find { it.name == scene.name }

        if (scene.name == Que.current()?.name && index == Que.currentIndex() && OBSState.currentSceneName == scene.name) {
            cell.background = if(isSelected) activeQueAndOBSSelectedColor else activeQueAndOBSColor
        } else if (scene.name == Que.current()?.name && index == Que.currentIndex()) {
            cell.background = if(isSelected) activeQueSelectedColor else activeQueColor
        } else if (OBSState.currentSceneName == scene.name) {
            cell.background = if(isSelected) activeOBSSelectedColor else activeOBSColor
        } else if (sceneExist == null) {
            cell.background = if (isSelected) nonExistingSelectedColor else nonExistingColor
        }

        return cell
    }
}