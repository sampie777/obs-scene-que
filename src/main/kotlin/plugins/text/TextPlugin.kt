package plugins.text

import gui.utils.createImageIcon
import plugins.common.BasePlugin
import plugins.common.QueItem
import plugins.text.queItems.HeaderQueItem
import plugins.text.queItems.PlainTextQueItem
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

@Suppress("unused")
class TextPlugin : BasePlugin {
    override val name = "TextPlugin"
    override val description = "Que items for just displaying text"
    override val version: String = "0.0.0"

    override val icon: Icon? = createImageIcon("/plugins/text/icon-14.png")

    override val tabName = "Text"

    internal val configStringSeparator = "|"

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Items")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        val itemListPanel = JPanel(GridLayout(0, 1))
        itemListPanel.add(HeaderQueItem.createPanelForQueItem(this))
        itemListPanel.add(PlainTextQueItem.createPanelForQueItem(this))

        val scrollPanelInnerPanel = JPanel(BorderLayout())
        scrollPanelInnerPanel.add(itemListPanel, BorderLayout.PAGE_START)
        val scrollPanel = JScrollPane(scrollPanelInnerPanel)
        scrollPanel.border = null
        panel.add(scrollPanel, BorderLayout.CENTER)
        return panel
    }

    override fun configStringToQueItem(value: String): QueItem {
        val className = value.substringBefore(configStringSeparator)
        val text = value.substringAfter(configStringSeparator)
        return when (className) {
            "HeaderQueItem" -> HeaderQueItem(this, text)
            "PlainTextQueItem" -> PlainTextQueItem(this, text)
            else -> throw IllegalArgumentException("Invalid TextPlugin que item: $value")
        }
    }
}