package gui.config.formcomponents

import config.Config
import themes.Theme
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.util.logging.Logger
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class BooleanFormInput(
    override val key: String,
    private val labelText: String,
    private val toolTipText: String = ""
) : FormInput {
    private val logger = Logger.getLogger(BooleanFormInput::class.java.name)

    private val input = JCheckBox()

    override fun component(): Component {
        val configValue: Boolean? = Config.get(key) as? Boolean

        val label = JLabel("<html>${labelText.replace("\n", "<br/>&nbsp;")}</html>")
        label.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 12)
        label.toolTipText = toolTipText

        input.isSelected = configValue ?: false
        input.preferredSize = Dimension(100, 20)
        input.horizontalAlignment = SwingConstants.CENTER
        input.alignmentX = Component.CENTER_ALIGNMENT
        input.toolTipText = toolTipText

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.toolTipText = toolTipText
        panel.add(label, BorderLayout.LINE_START)
        panel.add(input, BorderLayout.LINE_END)
        return panel
    }

    override fun validate(): List<String> {
        return ArrayList()
    }

    override fun save() {
        Config.set(key, value())
    }

    override fun value(): Boolean {
        return input.isSelected
    }
}