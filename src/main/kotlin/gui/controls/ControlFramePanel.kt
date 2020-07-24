package gui.controls

import GUI
import gui.Refreshable
import objects.que.Que
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.KeyEvent
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder

class ControlFramePanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(ControlFramePanel::class.java.name)

    private val previousQueItemButton = JButton("Previous")
    private val currentQueItemLabel = JLabel()
    val nextQueItemButton = JButton("Next")

    init {
        GUI.register(this)

        initGui()
        refreshScenes()
        switchedScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(5, 5, 5, 5)

        currentQueItemLabel.toolTipText = "Current queue item"
        currentQueItemLabel.font = Font("Dialog", Font.PLAIN, 26)
        currentQueItemLabel.horizontalAlignment = SwingConstants.CENTER
        currentQueItemLabel.alignmentX = Component.CENTER_ALIGNMENT

        previousQueItemButton.horizontalAlignment = SwingConstants.CENTER
        previousQueItemButton.alignmentX = Component.CENTER_ALIGNMENT
        previousQueItemButton.font = Font("Dialog", Font.PLAIN, 22)

        nextQueItemButton.horizontalAlignment = SwingConstants.CENTER
        nextQueItemButton.alignmentX = Component.CENTER_ALIGNMENT
        nextQueItemButton.font = Font("Dialog", Font.PLAIN, 26)

        previousQueItemButton.addActionListener { activatePreviousQueItem() }
        nextQueItemButton.addActionListener { activateNextQueItem() }

        previousQueItemButton.mnemonic = KeyEvent.VK_P
        nextQueItemButton.mnemonic = KeyEvent.VK_N

        val buttonPanel = JPanel()
        buttonPanel.layout = GridLayout(0, 1)
        buttonPanel.add(previousQueItemButton)
        buttonPanel.add(currentQueItemLabel)
        buttonPanel.add(nextQueItemButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    override fun windowClosing(window: Component?) {
        if (window !is ControlFrame) {
            return
        }

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

        currentQueItemLabel.text = "Current: ${Que.current()?.name}"
    }

    private fun refreshButtonsState() {
        previousQueItemButton.isEnabled = !Que.isFirstItem()
        nextQueItemButton.isEnabled = !Que.isLastItem()
    }

    private fun activateNextQueItem() {
        logger.info("Activate next queue item")
        Que.next()
    }

    private fun activatePreviousQueItem() {
        logger.info("Activate previous queue item")
        Que.previous()
    }
}