package gui.config.formcomponents

import config.Config
import gui.config.ConfigEditPanel
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.util.logging.Logger
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class StringFormInput(
    private val key: String,
    private val labelText: String,
    private val allowEmpty: Boolean
) : FormInput {
    private val logger = Logger.getLogger(ConfigEditPanel::class.java.name)

    private val input = JTextField()

    override fun component(): Component {
        val configValue: String? = Config.get(key) as? String

        val label = JLabel(labelText)
        label.font = Font("Dialog", Font.PLAIN, 12)

        input.text = configValue
        input.preferredSize = Dimension(100, 20)

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
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
        Config.set(key, value())
    }

    override fun value(): String {
        return input.text as String
    }
}