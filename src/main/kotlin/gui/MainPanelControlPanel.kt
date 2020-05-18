package gui

import GUI
import gui.quickAccessButtons.QuickAccessButtonPanel
import objects.que.Que
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class MainPanelControlPanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(MainPanelControlPanel::class.java.name)

    private val previousQueItemButton = JButton("Previous")
    private val nextQueItemButton = JButton("Next")

    init {
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

        previousQueItemButton.addActionListener { activatePreviousQueItem() }
        nextQueItemButton.addActionListener { activateNextQueItem() }

        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS)
        buttonPanel.alignmentX = Component.LEFT_ALIGNMENT
        buttonPanel.add(previousQueItemButton)
        buttonPanel.add(Box.createRigidArea(Dimension(0, 20)))
        buttonPanel.add(nextQueItemButton)

        val controlsPanel = JPanel()
        controlsPanel.layout = BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS)
        controlsPanel.add(buttonPanel)
        controlsPanel.add(Box.createRigidArea(Dimension(0, 20)))
        controlsPanel.add(QuickAccessButtonPanel(), Component.LEFT_ALIGNMENT)
        add(controlsPanel, BorderLayout.CENTER)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    override fun refreshQueItems() {
        switchedScenes()
    }

    override fun switchedScenes() {
        refreshButtonsState()

        val previousQueItem = Que.previewPrevious()
        if (previousQueItem == null) {
            previousQueItemButton.text = "Previous: none"
        } else {
            previousQueItemButton.text = "Previous: ${previousQueItem.name}"
        }

        val nextQueItem = Que.previewNext()
        if (nextQueItem == null) {
            nextQueItemButton.text = "Next: none"
        } else {
            nextQueItemButton.text = "Next: ${nextQueItem.name}"
        }
    }

    private fun refreshButtonsState() {
        previousQueItemButton.isEnabled = !Que.isFirstItem()
        nextQueItemButton.isEnabled = !Que.isLastItem()
    }

    private fun activateNextQueItem() {
        logger.info("Activate next que item")
        Que.next()
    }

    private fun activatePreviousQueItem() {
        logger.info("Activate previous que item")
        Que.previous()
    }
}