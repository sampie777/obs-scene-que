package plugins.utility.queItems

import GUI
import gui.list.QueListCellRenderer
import gui.mainFrame.MainFrame
import gui.utils.IconLabel
import gui.utils.createImageIcon
import handles.QueItemTransferHandler
import objects.que.JsonQueue
import objects.que.Que
import objects.que.QueItem
import plugins.utility.UtilityPlugin
import themes.Theme
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import java.util.Timer
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder


class DelayQueItem(override val plugin: UtilityPlugin, private val delay: Long) : QueItem {
    private val logger = Logger.getLogger(DelayQueItem::class.java.name)

    override val name: String = "Delay ($delay ms)"
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor
    override val icon: Icon? = DelayQueItem.icon

    private var delayTimer: Timer? = null
    private var timeStarted: Long = 0
    private var progressTimer: javax.swing.Timer = javax.swing.Timer(
        33, // 30 fps
        ActionListener { progressTimerUpdate() }
    )

    companion object {
        val icon: Icon? = createImageIcon("/plugins/utility/icon-delay-14.png")

        fun fromJson(plugin: UtilityPlugin, jsonQueueItem: JsonQueue.QueueItem): DelayQueItem {
            return DelayQueItem(plugin, jsonQueueItem.data["delay"]!!.toLong())
        }

        fun createPanelForQueItem(plugin: UtilityPlugin): JComponent {
            val panel = JPanel(BorderLayout(5, 5))
            panel.border = CompoundBorder(
                CompoundBorder(
                    EmptyBorder(5, 0, 5, 0),
                    BorderFactory.createMatteBorder(1, 1, 0, 1, Color(180, 180, 180))
                ),
                EmptyBorder(8, 10, 10, 10)
            )

            val inputSpinner = JSpinner(SpinnerNumberModel(3000L, 0L, Long.MAX_VALUE, 100L))
            inputSpinner.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
            inputSpinner.preferredSize = Dimension(inputSpinner.preferredSize.height, 10)

            val addButton = JButton("+")
            addButton.toolTipText = "Click or drag to add"
            addButton.addActionListener {
                val queItem = createQueItem(plugin, inputSpinner.value as Long)

                Que.add(queItem)
                GUI.refreshQueItems()
            }
            addButton.transferHandler = QueItemTransferHandler()
            addButton.addMouseMotionListener(object : MouseAdapter() {
                override fun mouseDragged(e: MouseEvent) {
                    val queItem = createQueItem(plugin, inputSpinner.value as Long)

                    val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                    transferHandler.queItem = queItem
                    transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
                }
            })

            panel.add(IconLabel(icon, "Delay (ms)"), BorderLayout.PAGE_START)
            panel.add(inputSpinner, BorderLayout.CENTER)
            panel.add(addButton, BorderLayout.LINE_END)
            return panel
        }

        private fun createQueItem(plugin: UtilityPlugin, value: Long): QueItem {
            return DelayQueItem(plugin, value)
        }
    }

    override fun activate() {
        logger.info("Starting Queue delay for $delay milli seconds")
        delayTimer = Timer()
        delayTimer!!.schedule(object : TimerTask() {
            override fun run() {
                delayTimerDoneTask()
            }
        }, delay)

        timeStarted = System.currentTimeMillis()
        progressTimer.restart()
    }

    private fun delayTimerDoneTask() {
        logger.info("Delay $name is over. Activating next Queue item")
        delayTimer = null

        progressTimer.stop()
        MainFrame.getInstance()?.repaint()

        Que.next()
    }

    override fun deactivate() {
        if (delayTimer == null) {
            logger.info("No delay timer to cancel")
            return
        }

        try {
            logger.info("Canceling delay timer")
            delayTimer!!.cancel()
            delayTimer = null
        } catch (e: Exception) {
            logger.info("Exception caught during canceling delay timer")
            e.printStackTrace()
        }
    }

    override fun toJson(): JsonQueue.QueueItem {
        val jsonQueItem = super.toJson()
        jsonQueItem.data["delay"] = delay.toString()
        return jsonQueItem
    }

    private fun progressTimerUpdate() {
        if (delayTimer == null) {
            progressTimer.stop()
        }

        MainFrame.getInstance()?.repaint()
    }

    override fun listCellRendererPaintAction(g: Graphics2D, queListCellRenderer: QueListCellRenderer) {
        super.listCellRendererPaintAction(g, queListCellRenderer)

        if (delayTimer == null) {
            return
        }

        val elapsed = System.currentTimeMillis() - timeStarted
        val progressBarPosition = queListCellRenderer.width * elapsed / delay

        val color = queListCellRenderer.background.darker().darker()
        g.color = Color(color.red, color.green, color.blue, 80)
        g.fillRect(0, 0, progressBarPosition.toInt(), 30)
    }
}