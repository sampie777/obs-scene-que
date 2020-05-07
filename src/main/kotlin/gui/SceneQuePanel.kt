package gui

import GUI
import config.Config
import handles.SceneTransferDropComponent
import handles.SceneTransferHandler
import objects.Globals
import objects.OBSClient
import objects.Que
import objects.TScene
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class SceneQuePanel : JPanel(), Refreshable, SceneTransferDropComponent {

    private val logger = Logger.getLogger(SceneQuePanel::class.java.name)

    val list: JList<TScene> = JList()
    val removeItemButton = JButton("Remove")
    val removeInvalidItemsButton = JButton("Remove Invalid")
    val removeAllButton = JButton("Remove All")

    init {
        name = "SceneQuePanel"
        GUI.register(this)

        initGui()

        list.setListData(Que.getList().toTypedArray())
        switchedScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 10, 10)

        val titleLabel = JLabel("Que")

        add(titleLabel, BorderLayout.PAGE_START)
        add(createQueListPanel(), BorderLayout.CENTER)
        add(createButtonPanel(), BorderLayout.PAGE_END)
    }

    private fun createQueListPanel(): JScrollPane {
        list.name = "QueList"
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.dropMode = DropMode.INSERT
        list.transferHandler = SceneTransferHandler()
        list.font = Font("Dialog", Font.PLAIN, 14)
        list.cursor = Cursor(Cursor.HAND_CURSOR)
        list.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(180, 180, 180)),
            EmptyBorder(10, 0, 0, 0)
        )
        list.cellRenderer = QueListCellRenderer()

        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val selectedIndex = (e.source as JList<*>).selectedIndex

                if (e.clickCount == 2) {   // On double click
                    logger.info("Set selected scene to live")
                    Que.setCurrentSceneByIndex(selectedIndex)
                    OBSClient.setActiveScene(Que.current() ?: return)
                }
            }
        })

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(350, 500)
        scrollPanel.border = null
        return scrollPanel
    }

    private fun createButtonPanel(): JPanel {
        removeItemButton.addActionListener { removeSelectedItem() }
        removeInvalidItemsButton.addActionListener { removeInvalidItems() }
        removeAllButton.addActionListener { removeAllItems() }

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
        buttonPanel.add(removeItemButton)
        buttonPanel.add(removeInvalidItemsButton)
        buttonPanel.add(removeAllButton)
        return buttonPanel
    }

    private fun removeSelectedItem() {
        Que.remove(list.selectedIndex)
        GUI.refreshQueScenes()
    }

    private fun removeInvalidItems() {
        Que.removeInvalidItems()
        GUI.refreshQueScenes()
    }

    private fun removeAllItems() {
        Que.clear()
        GUI.refreshQueScenes()
    }

    override fun refreshScenes() {
        list.repaint()
    }

    override fun refreshQueScenes() {
        list.setListData(Que.getList().toTypedArray())

        Config.save()

        switchedScenes()
    }

    override fun switchedScenes() {
        list.clearSelection()
        list.repaint()
    }

    override fun dropNewScene(scene: TScene, index: Int): Boolean {
        logger.info("Dropped new Scene: $scene at index: $index")

        Que.add(index, scene)

        GUI.refreshQueScenes()
        return true
    }

    override fun dropMoveScene(fromIndex: Int, toIndex: Int): Boolean {
        logger.info("Dropped moving Scene at index: $toIndex")

        val result = Que.move(fromIndex, toIndex)

        GUI.refreshQueScenes()
        return result
    }
}