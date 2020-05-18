package gui

import GUI
import handles.QueItemDropComponent
import handles.QueItemTransferHandler
import objects.que.Que
import plugins.common.QueItem
import themes.Theme
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class QuePanel : JPanel(), Refreshable, QueItemDropComponent {

    private val logger = Logger.getLogger(QuePanel::class.java.name)

    val list: JList<QueItem> = JList()
    val removeItemButton = JButton("Remove")
    val removeInvalidItemsButton = JButton("Remove Invalid")
    val removeAllButton = JButton("Remove All")

    init {
        name = "QuePanel"
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

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    private fun createQueListPanel(): JScrollPane {
        list.name = "QueList"
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.dropMode = DropMode.INSERT
        list.transferHandler = QueItemTransferHandler()
        list.font = Font("Dialog", Font.PLAIN, 14)
        list.cursor = Cursor(Cursor.HAND_CURSOR)
        list.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(180, 180, 180)),
            EmptyBorder(10, 0, 0, 0)
        )
        list.cellRenderer = QueListCellRenderer()
        list.background = Theme.get.QUE_LIST_BACKGROUND_COLOR

        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val selectedIndex = (e.source as JList<*>).selectedIndex

                if (e.clickCount == 2) {   // On double click
                    Que.setCurrentQueItemByIndex(selectedIndex)
                    Que.activateCurrent()
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
        GUI.refreshQueItems()
    }

    private fun removeInvalidItems() {
        Que.removeInvalidItems()
        GUI.refreshQueItems()
    }

    private fun removeAllItems() {
        Que.clear()
        GUI.refreshQueItems()
    }

    override fun refreshScenes() {
        list.repaint()
    }

    override fun refreshQueItems() {
        list.setListData(Que.getList().toTypedArray())

        Que.save()

        switchedScenes()
    }

    override fun switchedScenes() {
        list.clearSelection()
        list.repaint()
    }

    override fun dropNewItem(item: QueItem, index: Int): Boolean {
        logger.info("Dropped new QueItem: $item at index: $index")

        Que.add(index, item)

        GUI.refreshQueItems()
        return true
    }

    override fun dropMoveItem(item: QueItem, fromIndex: Int, toIndex: Int): Boolean {
        logger.info("Dropped moving QueItem at index: $toIndex")

        val result = Que.move(fromIndex, toIndex)

        GUI.refreshQueItems()
        return result
    }
}