package gui.config

import gui.config.formcomponents.*
import java.awt.BorderLayout
import java.awt.GridLayout
import java.util.logging.Logger
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class ConfigEditPanel : JPanel() {
    private val logger = Logger.getLogger(ConfigEditPanel::class.java.name)

    private val formComponents: ArrayList<FormComponent> = ArrayList()

    init {
        createFormInputs()
        createGui()
    }

    private fun createFormInputs() {
        formComponents.add(HeaderFormComponent("OBS"))
        formComponents.add(StringFormInput("obsAddress", "OBS websocket address", false))
        formComponents.add(StringFormInput("obsPassword", "OBS websocket password", true))
        formComponents.add(NumberFormInput<Long>("obsReconnectionTimeout", "Connection retry interval (millisec.)", 0, null))

        formComponents.add(HeaderFormComponent("GUI"))
        formComponents.add(BooleanFormInput("windowRestoreLastPosition", "Restore window position on start up"))
    }

    private fun createGui() {
        layout = BorderLayout()

        val mainPanel = JPanel()
        mainPanel.layout = GridLayout(0, 1)
        mainPanel.border = EmptyBorder(10, 10, 10, 10)

        addConfigItems(mainPanel)

        val scrollPanelInnerPanel = JPanel(BorderLayout())
        scrollPanelInnerPanel.add(mainPanel, BorderLayout.PAGE_START)
        val scrollPanel = JScrollPane(scrollPanelInnerPanel)
        scrollPanel.border = null
        add(scrollPanel, BorderLayout.CENTER)
    }

    private fun addConfigItems(panel: JPanel) {
        formComponents.forEach {
            panel.add(it.component())
        }
    }

    fun saveAll(): Boolean {
        val validationErrors = ArrayList<String>()
        formComponents.filterIsInstance<FormInput>()
            .forEach {
                val validation = it.validate()
                if (validation.isEmpty()) {
                    return@forEach
                }

                logger.warning(validation.toString())
                validationErrors += validation
            }

        if (validationErrors.isNotEmpty()) {
            if (this.parent == null) {
                logger.warning("Panel is not a visible GUI component")
                return false
            }

            JOptionPane.showMessageDialog(
                this, validationErrors.joinToString(",\n"),
                "Invalid data",
                JOptionPane.ERROR_MESSAGE
            )
            return false
        }

        formComponents.filterIsInstance<FormInput>()
            .forEach { it.save() }
        return true
    }
}
