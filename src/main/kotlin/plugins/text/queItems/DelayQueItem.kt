package plugins.text.queItems

import GUI
import handles.QueItemTransferHandler
import objects.que.JsonQue
import objects.que.Que
import objects.que.QueItem
import plugins.text.TextPlugin
import themes.Theme
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import java.util.Timer
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class DelayQueItem(override val plugin: TextPlugin, private val delay: Long) : QueItem {
    private val logger = Logger.getLogger(DelayQueItem::class.java.name)

    override val name: String = "Delay ($delay ms)"
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    private var timer: Timer? = null

    companion object {
        fun fromJson(plugin: TextPlugin, jsonQueItem: JsonQue.QueItem): DelayQueItem {
            return DelayQueItem(plugin, jsonQueItem.data["delay"]!!.toLong())
        }

        fun createPanelForQueItem(plugin: TextPlugin): JComponent {
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

            panel.add(JLabel("Delay (ms)"), BorderLayout.PAGE_START)
            panel.add(inputSpinner, BorderLayout.CENTER)
            panel.add(addButton, BorderLayout.LINE_END)
            return panel
        }

        private fun createQueItem(plugin: TextPlugin, value: Long): QueItem {
            return DelayQueItem(plugin, value)
        }
    }

    override fun activate() {
        logger.info("Starting Queue delay for $delay milli seconds")
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                logger.info("Delay $name is over. Activating next Queue item")
                timer = null
                Que.next()
            }
        }, delay)
    }

    override fun deactivate() {
        if (timer == null) {
            logger.info("No delay timer to cancel")
            return
        }

        try {
            logger.info("Canceling delay timer")
            timer!!.cancel()
        } catch (e: Exception) {
            logger.info("Exception caught during canceling delay timer")
            e.printStackTrace()
        }
    }

    override fun toJson(): JsonQue.QueItem {
        val jsonQueItem = super.toJson()
        jsonQueItem.data["delay"] = delay.toString()
        return jsonQueItem
    }
}