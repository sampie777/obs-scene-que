package gui.config

import config.PropertyLoader
import gui.config.formcomponents.*
import objects.notifications.Notifications
import themes.Theme
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
        formComponents.add(
            StringFormInput(
                "obsPassword",
                "OBS websocket password",
                true,
                toolTipText = "This value is not stored encrypted"
            )
        )
        formComponents.add(
            NumberFormInput<Long>(
                "obsReconnectionTimeout",
                "Connection retry interval (millisec.)",
                min = 0,
                max = null
            )
        )

        formComponents.add(HeaderFormComponent("GUI"))
        formComponents.add(ThemeSelectFormInput("theme", "Theme", Theme.availableThemes()))
        formComponents.add(BooleanFormInput("windowRestoreLastPosition", "Restore window position on start up"))
        formComponents.add(BooleanFormInput("mainWindowAlwaysOnTop", "Keep window always on top"))
        formComponents.add(
            NumberFormInput<Int>(
                "quickAccessButtonCount",
                "Quick Access buttons amount",
                min = 0,
                max = Int.MAX_VALUE
            )
        )
        formComponents.add(BooleanFormInput("quickAccessButtonDisplayIcon", "Quick Access Button display plugin icon"))

        formComponents.add(HeaderFormComponent("Queue"))
        formComponents.add(
            NativeKeyEventFormInput(
                "globalKeyEventPreviousQueueItem",
                "Global key for activating Previous Queue",
                allowEmpty = true,
                toolTipText = "Click the Assign button and hit your keys. Click again to clear."
            )
        )
        formComponents.add(
            NativeKeyEventFormInput(
                "globalKeyEventNextQueueItem",
                "Global key for activating Next Queue",
                allowEmpty = true,
                toolTipText = "Click the Assign button and hit your keys. Click again to clear."
            )
        )
        formComponents.add(
            BooleanFormInput(
                "activateNextSubQueueItemsOnMouseActivationQueueItem",
                "When activating a Queue Item,\nalso activate the next 'Activate After Previous'-Queue Items"
            )
        )
        formComponents.add(
            BooleanFormInput(
                "activateNextSubQueueItemsOnMouseActivationSubQueueItem",
                "When activating an 'Activate After Previous'-Queue Item,\nalso activate the next 'Activate After Previous'-Queue Items"
            )
        )
        formComponents.add(StringFormInput("pluginDirectory", "Plugin directory", false))

        formComponents.add(HeaderFormComponent("Web control"))
        formComponents.add(
            BooleanFormInput(
                "httpApiServerEnabled",
                "Enable Queue web control server",
                toolTipText = "This will run an HTTP server which can be used to interact with the Queue"
            )
        )
        formComponents.add(
            NumberFormInput<Int>(
                "httpApiServerPort",
                "Web control server port",
                min = 0,
                max = null
            )
        )

        formComponents.add(HeaderFormComponent("Logging"))
        formComponents.add(
            BooleanFormInput(
                "enableApplicationLoggingToFile",
                "Enable application logging to a file",
                toolTipText = "Location of this logfile is shown in the Information screen"
            )
        )

        formComponents.add(HeaderFormComponent("Other"))
        formComponents.add(BooleanFormInput("updatesCheckForUpdates", "Check for updates"))
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
            try {
                panel.add(it.component())
            } catch (e: Exception) {
                logger.severe("Failed to create Config Edit GUI component: ${it::class.java}")
                e.printStackTrace()

                if (it !is FormInput) {
                    return@forEach
                }

                logger.severe("Failed to create Config Edit GUI component: ${it.key}")
                Notifications.add(
                    "Failed to load GUI input for config key: <strong>${it.key}</strong>. Delete your <i>${PropertyLoader.getPropertiesFile().name}</i> file and try again. (Error: ${e.localizedMessage})",
                    "Configuration"
                )
                panel.add(TextFormComponent("Failed to load component. See Notifications.").component())
            }
        }
    }

    fun saveAll(): Boolean {
        val formInputComponents = formComponents.filterIsInstance<FormInput>()
        val validationErrors = ArrayList<String>()

        formInputComponents.forEach {
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

        formInputComponents.forEach { it.save() }
        return true
    }
}
