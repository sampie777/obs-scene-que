package gui

import GUI
import objects.OBSClient
import objects.Que
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*

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
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val titleLabel = JLabel("Controls")
        add(titleLabel)

        add(Box.createRigidArea(Dimension(0, 20)))

        previousSceneButton.addActionListener { setPreviousQueSceneLive() }
        add(previousSceneButton)

        add(Box.createRigidArea(Dimension(0, 20)))

        nextSceneButton.addActionListener { setNextQueSceneLive() }
        add(nextSceneButton)
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