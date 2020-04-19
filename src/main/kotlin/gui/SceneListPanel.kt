package gui

import handles.SceneTransferHandler
import objects.Globals
import objects.TScene
import java.awt.Dimension
import javax.swing.*
import kotlin.streams.toList

class SceneListPanel : JPanel(), Refreshable {

    private val list: JList<TScene> = JList()

    init {
        name = "SceneListPanel"
        GUI.register(this)

        initGui()
        refreshScenes()
    }

    private fun initGui() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("Available scenes")
        add(titleLabel)

        add(Box.createRigidArea(Dimension(0, 20)))

        list.name = "SceneList"
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.transferHandler = SceneTransferHandler()

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = null
        add(scrollPanel)
    }

    override fun refreshScenes() {
        list.setListData(Globals.scenes.values.stream()
            .sorted { tScene, tScene2 -> tScene.name.compareTo(tScene2.name) }
            .toList().toTypedArray()
        );
    }
}