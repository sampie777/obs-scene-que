package gui.config

import config.Config
import gui.HotKeysMapping
import gui.mainFrame.MainFrame
import gui.utils.addHotKeyMapping
import themes.Theme
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class ConfigActionPanel(private val frame: ConfigFrame) : JPanel() {
    private val logger = Logger.getLogger(ConfigActionPanel::class.java.name)

    val saveButton = JButton("Save")

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        saveButton.addActionListener { saveConfigAndClose() }
        saveButton.addHotKeyMapping(HotKeysMapping.SAVE_BUTTON)
        frame.rootPane.defaultButton = saveButton

        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { cancelWindow() }
        cancelButton.addHotKeyMapping(HotKeysMapping.CANCEL_BUTTON)

        add(Box.createHorizontalGlue())
        add(saveButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(cancelButton)
    }

    private fun cancelWindow() {
        logger.fine("Exiting configuration window")
        frame.dispose()
    }

    private fun saveConfigAndClose() {
        logger.info("Saving configuration changes")
        if (!frame.saveAll()) {
            return
        }

        Config.save()
        frame.dispose()
        Theme.init()
        MainFrame.getInstance()?.rebuildGui()
    }
}
