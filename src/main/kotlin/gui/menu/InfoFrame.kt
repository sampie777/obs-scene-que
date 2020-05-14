package gui.menu

import gui.utils.ClickableLinkComponent
import objects.ApplicationInfo
import themes.Theme
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

        val versionLabel = JLabel("<html><h1>${ApplicationInfo.name}</h1><p>By ${ApplicationInfo.author}</p><p>Version: ${ApplicationInfo.version}</p></html>")
        versionLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)
        val sourceCodeLabel = ClickableLinkComponent(
            "${ApplicationInfo.name} on BitBucket",
            "https://github.com/sampie777/obs-scene-que"
        )
        sourceCodeLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)

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