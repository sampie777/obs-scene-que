package gui.utils


import GUI
import handles.QueItemTransferHandler
import objects.que.Que
import objects.que.QueItem
import java.awt.Cursor
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.JList
import javax.swing.ListSelectionModel
import javax.swing.border.EmptyBorder

class DefaultSourcesList<T> : JList<T> {
    constructor() : super()
    constructor(values: Array<T>) : super(values)

    private val logger = Logger.getLogger(DefaultSourcesList::class.java.name)

    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        dragEnabled = true
        transferHandler = QueItemTransferHandler()
        font = Font("Dialog", Font.PLAIN, 14)
        cursor = Cursor(Cursor.HAND_CURSOR)
        border = EmptyBorder(10, 10, 0, 10)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if ((e.source as JList<*>).selectedValue == null) {
                    return
                }

                // On double click
                if (e.clickCount == 2) {
                    logger.info("[MouseEvent] Mouse Double click on list")

                    val queItem = (e.source as JList<*>).selectedValue as QueItem
                    Que.add(queItem)
                    GUI.refreshQueItems()
                }
            }
        })
    }
}