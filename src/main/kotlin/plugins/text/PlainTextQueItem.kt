package plugins.text

import GUI
import handles.QueItemTransferHandler
import objects.que.Que
import plugins.common.QueItem
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class PlainTextQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    companion object {
        fun createPanelForQueItem(plugin: TextPlugin) : JComponent {
            val panel = JPanel(BorderLayout(10, 10))
            panel.border = CompoundBorder(
                CompoundBorder(
                    EmptyBorder(5, 0, 5, 0),
                    BorderFactory.createLineBorder(Color(180, 180, 180))
                ),
                EmptyBorder(10, 10, 10, 10)
            )

            val textField = JTextField()

            val addButton = JButton("+")
            addButton.toolTipText = "Click or drag to add"
            addButton.addActionListener {
                if (textField.text.isEmpty()) {
                    return@addActionListener
                }

                val queItem = PlainTextQueItem(plugin, textField.text)
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

                    val queItem = PlainTextQueItem(plugin, textField.text)
                    textField.text = ""

                    val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                    transferHandler.queItem = queItem
                    transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
                }
            })

            panel.add(JLabel("Plain text"), BorderLayout.PAGE_START)
            panel.add(textField, BorderLayout.CENTER)
            panel.add(addButton, BorderLayout.LINE_END)
            return panel
        }
    }

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name
}