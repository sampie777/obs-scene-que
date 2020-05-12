package gui.config.formcomponents

import themes.Theme
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel

class TextFormComponent(private val text: String) : FormComponent {
    override fun component(): Component {
        val label = JLabel(text)
        label.font = Font(Theme.get.FONT_FAMILY, Font.ITALIC, 12)

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.add(label, BorderLayout.CENTER)
        return panel
    }
}