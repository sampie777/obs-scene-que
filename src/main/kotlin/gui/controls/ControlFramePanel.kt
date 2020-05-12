package gui.controls

import GUI
import gui.Refreshable
import objects.OBSClient
import objects.OBSState
import objects.Que
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import java.awt.GridLayout
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder

class ControlFramePanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(ControlFramePanel::class.java.name)

    private val previousSceneButton = JButton("Previous")
    private val currentSceneLabel = JLabel()
    private val nextSceneButton = JButton("Next")

    init {
        name = "ControlFramePanel"
        GUI.register(this)

        initGui()
        refreshScenes()
        switchedScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(5, 5, 5, 5)

        currentSceneLabel.toolTipText = "Current scene"
        currentSceneLabel.font = Font("Dialog", Font.PLAIN, 26)
        currentSceneLabel.horizontalAlignment = SwingConstants.CENTER
        currentSceneLabel.alignmentX = Component.CENTER_ALIGNMENT

        previousSceneButton.horizontalAlignment = SwingConstants.CENTER
        previousSceneButton.alignmentX = Component.CENTER_ALIGNMENT
        previousSceneButton.font = Font("Dialog", Font.PLAIN, 22)

        nextSceneButton.horizontalAlignment = SwingConstants.CENTER
        nextSceneButton.alignmentX = Component.CENTER_ALIGNMENT
        nextSceneButton.font = Font("Dialog", Font.PLAIN, 26)

        previousSceneButton.addActionListener { setPreviousQueSceneLive() }
        nextSceneButton.addActionListener { setNextQueSceneLive() }

        val buttonPanel = JPanel()
        buttonPanel.layout = GridLayout(0, 1)
        buttonPanel.add(previousSceneButton)
        buttonPanel.add(currentSceneLabel)
        buttonPanel.add(nextSceneButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun refreshQueScenes() {
        switchedScenes()
    }

    override fun switchedScenes() {
        refreshButtonsState()

        val previousScene = Que.previewPrevious()
        if (previousScene == null) {
            previousSceneButton.text = "Previous: none"
        } else {
            previousSceneButton.text = "Previous: ${previousScene.name}"
        }

        val nextScene = Que.previewNext()
        if (nextScene == null) {
            nextSceneButton.text = "Next: none"
        } else {
            nextSceneButton.text = "Next: ${nextScene.name}"
        }

        currentSceneLabel.text = "Current: ${OBSState.currentSceneName}"
    }

    private fun refreshButtonsState() {
        previousSceneButton.isEnabled = !Que.isFirstItem()
        nextSceneButton.isEnabled = !Que.isLastItem()
    }

    private fun setNextQueSceneLive() {
        logger.info("Set next scene to live")
        OBSClient.setActiveScene(Que.next() ?: return)
    }

    private fun setPreviousQueSceneLive() {
        logger.info("Set previous scene to live")
        OBSClient.setActiveScene(Que.previous() ?: return)
    }
}