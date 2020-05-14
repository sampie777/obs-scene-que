package gui

import GUI
import config.Config
import config.PropertyLoader
import objects.OBSClientStatus
import objects.OBSState
import themes.Theme
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel

class OBSStatusPanel : JPanel(), Refreshable {

    private val messageLabel: JLabel = JLabel()

    init {
        GUI.register(this)
        initGUI()
        refreshOBSStatus()
    }

    private fun initGUI() {
        layout = BorderLayout(15, 15)

        messageLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)
        messageLabel.toolTipText = settingsFileString()
        add(messageLabel)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    fun getMessageLabel(): JLabel {
        return messageLabel
    }

    override fun refreshOBSStatus() {
        messageLabel.text = "OBS: ${getOBSStatusRepresentation()}"

        if (OBSState.connectionStatus == OBSClientStatus.CONNECTED) {
            messageLabel.toolTipText = "Connected to ${Config.obsAddress}. ${settingsFileString()}"
        } else {
            messageLabel.toolTipText = settingsFileString()
        }
        repaint()
    }

    fun getOBSStatusRepresentation(): String {
        val obsDisplayStatus = if (OBSState.clientActivityStatus != null)
            OBSState.clientActivityStatus else OBSState.connectionStatus

        var obsDisplayStatusString = obsDisplayStatus!!.status
        if (obsDisplayStatus == OBSClientStatus.CONNECTING) {
            obsDisplayStatusString = "Connecting to ${Config.obsAddress}..."
        }

        return obsDisplayStatusString
    }

    private fun settingsFileString(): String {
        return "Settings file: " + PropertyLoader.getPropertiesFile().absolutePath
    }
}