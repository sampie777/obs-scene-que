package gui.config.formcomponents

import config.Config
import themes.Theme
import themes.ThemeWrapper
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.util.logging.Logger
import javax.swing.*

class ThemeSelectFormInput(
    override val key: String,
    private val labelText: String,
    private val values: List<ThemeWrapper>
) : FormInput {

    private val logger = Logger.getLogger(ThemeSelectFormInput::class.java.name)

    private val selectBox = JComboBox<ThemeWrapper>()

    override fun component(): Component {
        val label = JLabel(labelText)
        label.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 12)

        selectBox.model = DefaultComboBoxModel(values.toTypedArray())
        selectBox.selectedItem = values.find { it.internalName == Config.get(key) }
        selectBox.preferredSize = Dimension(100, 30)
        selectBox.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.add(label, BorderLayout.LINE_START)
        panel.add(selectBox, BorderLayout.LINE_END)
        return panel
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()

        if (values.find { it.internalName == value().internalName } == null) {
            errors.add("Selected invalid value '${value().displayName}' for '$labelText'")
        }

        return errors
    }

    override fun save() {
        Config.set(key, value().internalName)
    }

    override fun value(): ThemeWrapper {
        return selectBox.selectedItem as ThemeWrapper
    }
}