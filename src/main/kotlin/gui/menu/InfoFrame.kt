package gui.menu

import gui.utils.ClickableLinkComponent
import java.awt.Dimension
import java.awt.Font
import javax.swing.*
import javax.swing.border.EmptyBorder

class InfoFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {

    init {
        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.PAGE_AXIS)
        mainPanel.border = EmptyBorder(0, 20, 10, 20)
        add(mainPanel)

        val currentRelease = "1.3.1"
        val versionLabel = JLabel("<html><h1>OBS Scene Que</h1><p>By Samuel-Anton Jansen</p><p>Version: $currentRelease</p></html>")
        versionLabel.font = Font("Dialog", Font.PLAIN, 14)
        val sourceCodeLabel = ClickableLinkComponent(
            "OBS Scene Que on BitBucket",
            "https://github.com/sampie777/obs-scene-que"
        )
        sourceCodeLabel.font = Font("Dialog", Font.PLAIN, 14)

        mainPanel.add(versionLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(sourceCodeLabel)

        title = "Information"
        setSize(400, 160)
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
        isResizable = false
        isVisible = true
    }
}