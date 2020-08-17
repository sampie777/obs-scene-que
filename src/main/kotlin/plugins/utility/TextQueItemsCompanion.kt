package plugins.utility

import GUI
import gui.utils.IconLabel
import gui.utils.createImageIcon
import handles.QueItemTransferHandler
import objects.que.Que
import objects.que.QueItem
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

abstract class TextQueItemsCompanion(private val queItemClass: Class<*>, private val itemTitle: String) {

    val icon: Icon? = createImageIcon("/plugins/utility/icon-text-14.png")

    open fun createPanelForQueItem(plugin: UtilityPlugin): JComponent {
        val panel = JPanel(BorderLayout(5, 5))
        panel.border = CompoundBorder(
            CompoundBorder(
                EmptyBorder(5, 0, 5, 0),
                BorderFactory.createMatteBorder(1, 1, 0, 1, Color(180, 180, 180))
            ),
            EmptyBorder(8, 10, 10, 10)
        )

        val textField = JTextField()

        val addButton = JButton("+")
        addButton.toolTipText = "Click or drag to add"
        addButton.addActionListener {
            if (textField.text.isEmpty()) {
                return@addActionListener
            }

            val queItem = createQueItem(plugin, textField.text)
            textField.text = ""

            Que.add(queItem)
            GUI.refreshQueItems()
        }
        addButton.transferHandler = QueItemTransferHandler()
        addButton.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                if (textField.text.isEmpty()) {
                    return
                }

                val queItem = createQueItem(plugin, textField.text)
                textField.text = ""

                val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                transferHandler.queItem = queItem
                transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
            }
        })

        panel.add(IconLabel(icon, itemTitle), BorderLayout.PAGE_START)
        panel.add(textField, BorderLayout.CENTER)
        panel.add(addButton, BorderLayout.LINE_END)
        return panel
    }

    private fun createQueItem(plugin: UtilityPlugin, value: String): QueItem {
        val classDefinition = Class.forName(queItemClass.name)
        val cons = classDefinition.constructors[0]
        return cons.newInstance(plugin, value) as QueItem
    }
}