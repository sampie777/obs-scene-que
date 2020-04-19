package gui

import GUI
import config.Config
import handles.SceneTransferDropComponent
import handles.SceneTransferHandler
import objects.Globals
import objects.OBSClient
import objects.Que
import objects.TScene
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.*

class SceneQuePanel : JPanel(), Refreshable, SceneTransferDropComponent {

    private val logger = Logger.getLogger(SceneQuePanel::class.java.name)

    private val list: JList<TScene> = JList()
    private val removeItemButton = JButton("Remove")

    init {
        name = "SceneQuePanel"
        GUI.register(this)

        initGui()
        refreshQueScenes()
    }

    private fun initGui() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("Que")

        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.add(titleLabel, BorderLayout.PAGE_START)
        mainPanel.add(createQueListPanel(), BorderLayout.CENTER)
        mainPanel.add(createButtonPanel(), BorderLayout.PAGE_END)
        add(mainPanel)
    }

    private fun createQueListPanel(): JScrollPane {
        list.name = "QueList"
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.dropMode = DropMode.INSERT
        list.transferHandler = SceneTransferHandler()

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
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = null
        return scrollPanel
    }

    private fun createButtonPanel(): JPanel {
        removeItemButton.addActionListener { removeSelectedItem() }

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
        buttonPanel.add(removeItemButton)
        return buttonPanel
    }

    private fun removeSelectedItem() {
        Que.remove(list.selectedIndex)
        GUI.refreshQueScenes()
    }

    override fun refreshQueScenes() {
        list.setListData(Que.getList().toTypedArray())

        Config.save()

        switchedScenes()
    }

    override fun switchedScenes() {
        list.selectedIndex = getNewSelectedIndex()
    }

    private fun getNewSelectedIndex(): Int {
        if (Globals.activeOBSSceneName == null || Que.current() == null) {
            return -1
        }

        if (Globals.activeOBSSceneName != Que.current()!!.name) {
            return -1
        }

        return Que.currentIndex()
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