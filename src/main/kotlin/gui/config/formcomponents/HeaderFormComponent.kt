package gui.config.formcomponents

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class HeaderFormComponent(private val text: String) : FormComponent {
    override fun component(): Component {
        val label = JLabel(text)
        label.font = Font("Dialog", Font.BOLD, 12)
        label.horizontalAlignment = SwingConstants.CENTER
        label.alignmentX = Component.CENTER_ALIGNMENT

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.add(label, BorderLayout.CENTER)
        return panel
    }
}