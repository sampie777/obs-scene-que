package gui.config.formcomponents

import config.Config
import gui.config.ConfigEditPanel
import java.awt.*
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JLabel
import javax.swing.JPanel

class ColorFormInput(
    private val key: String,
    private val labelText: String
) : FormInput {
    private val logger = Logger.getLogger(ConfigEditPanel::class.java.name)

    private lateinit var color: Color
    private val button = JButton()
    private val panel = JPanel()

    override fun component(): Component {
        color = Config.get(key) as? Color ?: Color.BLACK

        val label = JLabel(labelText)
        label.font = Font("Dialog", Font.PLAIN, 12)

        button.text = "Choose"
        button.background = color
        button.preferredSize = Dimension(100, 30)
        button.addActionListener {
            chooseColor()
        }

        panel.layout = BorderLayout(10, 10)
        panel.add(label, BorderLayout.LINE_START)
        panel.add(button, BorderLayout.LINE_END)
        return panel
    }

    private fun chooseColor() {
        color = JColorChooser.showDialog(
            this.panel,
            "Choose Color",
            color
        )
            ?: return
        button.background = color
    }

    override fun validate(): List<String> {
        return ArrayList()
    }

    override fun save() {
        Config.set(key, value())
    }

    override fun value(): Color {
        return color
    }
}