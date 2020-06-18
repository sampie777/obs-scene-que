package gui.plugins.config.formInputs

import gui.config.formcomponents.FormInput
import themes.Theme
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

@Suppress("unused")
class StringFormInput(
    override val key: String,
    private val value: String,
    private val saveCallback: (value: String) -> Unit,
    private val labelText: String,
    private val allowEmpty: Boolean,
    private val toolTipText: String = ""
) : FormInput {
    private val logger = Logger.getLogger(StringFormInput::class.java.name)

    private val input = JTextField()

    override fun component(): Component {
        val label = JLabel(labelText)
        label.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 12)
        label.toolTipText = toolTipText

        input.text = value
        input.preferredSize = Dimension(100, 20)
        input.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        input.toolTipText = toolTipText

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.toolTipText = toolTipText
        panel.add(label, BorderLayout.LINE_START)
        panel.add(input, BorderLayout.CENTER)
        return panel
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()

        if (!allowEmpty && value().isEmpty()) {
            errors.add("Value for '$labelText' may not be empty")
        }

        return errors
    }

    override fun save() {
        saveCallback.invoke(value())
    }

    override fun value(): String {
        return input.text as String
    }
}