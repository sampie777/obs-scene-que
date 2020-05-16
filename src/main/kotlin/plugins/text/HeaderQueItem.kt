package plugins.text

import GUI
import objects.que.Que
import plugins.common.QueItem
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class HeaderQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    companion object {
        fun createPanelForQueItem(plugin: TextPlugin): JComponent {
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
            addButton.addActionListener {
                if (textField.text.isEmpty()) {
                    return@addActionListener
                }

                val queItem = HeaderQueItem(plugin, textField.text)
                textField.text = ""

                Que.add(queItem)
                GUI.refreshQueItems()
            }

            panel.add(JLabel("Header"), BorderLayout.PAGE_START)
            panel.add(textField, BorderLayout.CENTER)
            panel.add(addButton, BorderLayout.LINE_END)
            return panel
        }
    }

    override fun activate() {
        // Skip this que item by going to the next one
        Que.next()
    }

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        super.getListCellRendererComponent(cell, index, isSelected = false, cellHasFocus = false)
        cell.background = Color(200, 200, 200)
    }

}