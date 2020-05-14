package gui

import GUI
import handles.SceneTransferHandler
import objects.OBSState
import objects.TScene
import java.awt.*
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class SceneListPanel : JPanel(), Refreshable {

    private val list: JList<TScene> = JList()

    init {
        name = "SceneListPanel"
        GUI.register(this)

        initGui()
        refreshScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Available scenes")
        add(titleLabel, BorderLayout.PAGE_START)

        list.name = "SceneList"
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.transferHandler = SceneTransferHandler()
        list.background = null
        list.font = Font("Dialog", Font.PLAIN, 14)
        list.cursor = Cursor(Cursor.HAND_CURSOR)
        list.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(180, 180, 180)),
            EmptyBorder(10, 10, 0, 10)
        )

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = null
        add(scrollPanel, BorderLayout.CENTER)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    override fun refreshScenes() {
        list.setListData(OBSState.scenes.toTypedArray());
    }
}