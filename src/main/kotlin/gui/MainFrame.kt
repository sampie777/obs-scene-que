package gui

import GUI
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class MainFrame : JFrame(), Refreshable {
    private val logger = Logger.getLogger(MainFrame::class.java.name)

    init {
        GUI.register(this)
        initGUI()
    }

    private fun initGUI() {
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout(10, 10)
        mainPanel.border = EmptyBorder(10, 10, 10, 10)
        add(mainPanel)

        val leftPanel = JPanel()
        leftPanel.layout = BorderLayout(10, 10)
        leftPanel.add(SceneListPanel(), BorderLayout.CENTER)
        leftPanel.add(OBSStatusPanel(), BorderLayout.PAGE_END)

        val rightPanel = JPanel()
        rightPanel.layout = BorderLayout(10, 10)
        rightPanel.add(SceneQuePanel(), BorderLayout.LINE_START)
        rightPanel.add(SceneLiveControlPanel(), BorderLayout.CENTER)

        mainPanel.add(leftPanel, BorderLayout.LINE_START)
        mainPanel.add(rightPanel , BorderLayout.CENTER)

        setSize(900, 600)
        title = "OBS Scene Que"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }
}