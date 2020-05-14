package gui

import GUI
import objects.OBSClient
import objects.Que
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class SceneLiveControlPanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(SceneLiveControlPanel::class.java.name)

    private val previousSceneButton = JButton("Previous")
    private val nextSceneButton = JButton("Next")

    init {
        name = "SceneLiveControlPanel"
        GUI.register(this)

        initGui()
        refreshScenes()
        switchedScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 10, 10)

        val titleLabel = JLabel("Controls")
        add(titleLabel, BorderLayout.PAGE_START)

        previousSceneButton.addActionListener { setPreviousQueSceneLive() }
        nextSceneButton.addActionListener { setNextQueSceneLive() }

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS)
        buttonPanel.add(previousSceneButton)
        buttonPanel.add(Box.createRigidArea(Dimension(0, 20)))
        buttonPanel.add(nextSceneButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
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