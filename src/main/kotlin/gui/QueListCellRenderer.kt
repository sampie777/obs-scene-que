package gui

import objects.Globals
import objects.Que
import objects.TScene
import java.awt.Color
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.border.EmptyBorder

class QueListCellRenderer : DefaultListCellRenderer() {

    val selectedColor = Color(184, 207, 229)
    val activeOBSColor = Color(200, 230, 255)
    val activeOBSSelectedColor = Color(180, 200, 235)
    val activeQueColor = Color(225, 253, 225)
    val activeQueSelectedColor = Color(100, 225, 100)
    val activeQueAndOBSColor = Color(180, 255, 180)
    val activeQueAndOBSSelectedColor = Color(100, 225, 100)
    val nonExistingColor = Color(230, 230, 230)
    val nonExistingSelectedColor = Color(190, 190, 190)

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
        val sceneExist = Globals.scenes.find { it.name == scene.name }

        if (scene.name == Que.current()?.name && index == Que.currentIndex() && Globals.activeOBSSceneName == scene.name) {
            cell.background = if(isSelected) activeQueAndOBSSelectedColor else activeQueAndOBSColor
        } else if (scene.name == Que.current()?.name && index == Que.currentIndex()) {
            cell.background = if(isSelected) activeQueSelectedColor else activeQueColor
        } else if (Globals.activeOBSSceneName == scene.name) {
            cell.background = if(isSelected) activeOBSSelectedColor else activeOBSColor
        } else if (sceneExist == null) {
            cell.background = if (isSelected) nonExistingSelectedColor else nonExistingColor
        }

        return cell
    }
}