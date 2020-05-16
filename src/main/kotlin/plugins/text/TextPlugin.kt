package plugins.text

import gui.utils.createImageIcon
import plugins.common.BasePlugin
import plugins.common.QueItem
import java.awt.BorderLayout
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

@Suppress("unused")
class TextPlugin : BasePlugin {
    override val name = "TextPlugin"
    override val description = "Que items for just displaying text"

    override val icon: Icon? = createImageIcon("/plugins/text/icon-14.png")

    override val tabName = "Text"

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("This plugin is inaccessible")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        return panel
    }

    override fun configStringToQueItem(value: String): QueItem {
        return PlainTextQueItem(this, value)
    }
}