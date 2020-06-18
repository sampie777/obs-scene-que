package plugins.text

import gui.utils.createImageIcon
import objects.que.JsonQue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import plugins.text.queItems.DelayQueItem
import plugins.text.queItems.HeaderQueItem
import plugins.text.queItems.PlainTextQueItem
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

@Suppress("unused")
class TextPlugin : QueItemBasePlugin {
    override val name = "TextPlugin"
    override val description = "Queue items for just displaying text or adding delay"
    override val version: String = "0.0.0"

    override val icon: Icon? = createImageIcon("/plugins/text/icon-14.png")

    override val tabName = "Text & Delay"

    internal val configStringSeparator = "|"
    internal val quickAccessColor = Color(231, 231, 231)

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Items")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        val itemListPanel = JPanel(GridLayout(0, 1))
        itemListPanel.add(HeaderQueItem.createPanelForQueItem(this))
        itemListPanel.add(PlainTextQueItem.createPanelForQueItem(this))
        itemListPanel.add(DelayQueItem.createPanelForQueItem(this))

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
            HeaderQueItem::class.java.simpleName -> HeaderQueItem(this, text)
            PlainTextQueItem::class.java.simpleName -> PlainTextQueItem(this, text)
            else -> throw IllegalArgumentException("Invalid TextPlugin queue item: $value")
        }
    }

    override fun jsonToQueItem(jsonQueItem: JsonQue.QueItem): QueItem {
        return when (jsonQueItem.className) {
            HeaderQueItem::class.java.simpleName -> HeaderQueItem(this, jsonQueItem.name)
            PlainTextQueItem::class.java.simpleName -> PlainTextQueItem(this, jsonQueItem.name)
            DelayQueItem::class.java.simpleName -> DelayQueItem.fromJson(this, jsonQueItem)
            else -> throw IllegalArgumentException("Invalid TextPlugin queue item: ${jsonQueItem.className}")
        }
    }
}