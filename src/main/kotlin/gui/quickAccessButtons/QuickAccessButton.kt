package gui.quickAccessButtons

import brightness
import com.google.gson.Gson
import config.Config
import gui.utils.createGraphics
import gui.utils.isCtrlClick
import gui.utils.setDefaultRenderingHints
import handles.QueItemDropComponent
import handles.QueItemTransferHandler
import objects.notifications.Notifications
import objects.que.QueItem
import themes.Theme
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.TransferHandler
import javax.swing.border.CompoundBorder
import javax.swing.border.LineBorder

class QuickAccessButton(
    private val index: Int,
    private var queItem: QueItem? = null
) : JButton(), QueItemDropComponent {

    private val logger = Logger.getLogger(QuickAccessButton::class.java.name)

    private val defaultSize = Dimension(100, 100)
    private val iconMargin = 5
    private val iconSize = 15
    private val iconOpacity = 0.5f
    private var iconBufferedImage = iconBufferedImage()

    init {
        size = defaultSize
        minimumSize = defaultSize
        preferredSize = defaultSize
        transferHandler = QueItemTransferHandler()
        addActionListener { e -> onClick(e) }
        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val queItem = queItem ?: return

                val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                transferHandler.queItem = queItem
                transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
            }
        })

        refreshLayout()
    }

    override fun dropNewItem(item: QueItem, index: Int): Boolean {
        setNewItem(item)
        return true
    }

    override fun dropMoveItem(item: QueItem, fromIndex: Int, toIndex: Int): Boolean {
        setNewItem(item.clone())
        return true
    }

    private fun setNewItem(item: QueItem?) {
        queItem = item
        refreshLayout()

        if (item == null) {
            Config.quickAccessButtonQueItems[index] = ""
        } else {
            Config.quickAccessButtonQueItems[this.index] = Gson().toJson(item.toJson())
        }
    }

    private fun refreshLayout() {
        if (queItem == null) {
            text = "empty"
            toolTipText = "Assign queue item by drag-and-drop"
            background = null
            isEnabled = false
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            return
        }

        text = "<html><center>${queItem?.name}</center></html>"
        toolTipText = queItem?.plugin?.name
        isEnabled = true
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        iconBufferedImage = iconBufferedImage()

        if (queItem?.quickAccessColor == null) {
            background = Theme.get.BUTTON_BACKGROUND_COLOR
        } else {
            // Check for light/dark theme
            if (brightness(Theme.get.BUTTON_BACKGROUND_COLOR) > 110) {
                background = queItem?.quickAccessColor
            } else {
                background = Theme.get.BUTTON_BACKGROUND_COLOR
                foreground = queItem?.quickAccessColor
                border = CompoundBorder(
                    LineBorder(queItem?.quickAccessColor?.darker()),
                    LineBorder(queItem?.quickAccessColor?.darker()?.darker())
                )
            }
        }
    }

    fun getQueItem(): QueItem? = queItem

    private fun onClick(e: ActionEvent) {
        if (isCtrlClick(e.modifiers)) {
            clearItem()
        } else {
            activate()
        }
    }

    fun clearItem() {
        setNewItem(null)
    }

    private fun activate() {
        if (queItem == null) {
            logger.info("No quick access item specified to activate")
            return
        }

        try {
            queItem!!.activate()
        } catch (e: Exception) {
            logger.warning("Failed to activate quick access item: $queItem")
            e.printStackTrace()
            Notifications.add("Failed to activate '${queItem?.name}'", "QuickAccessButton")
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        if (queItem == null || iconBufferedImage == null || !Config.quickAccessButtonDisplayIcon) {
            return
        }

        val g2 = g as Graphics2D
        setDefaultRenderingHints(g2)

        g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, iconOpacity)
        g2.drawImage(iconBufferedImage, width - iconSize - iconMargin, iconMargin, iconSize, iconSize, null)
        g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F)
    }

    private fun iconBufferedImage(): BufferedImage? {
        if (queItem == null) {
            return null
        }

        val icon = queItem?.plugin?.icon ?: return null

        val (bufferedImage, g2) = createGraphics(icon.iconWidth, icon.iconHeight)
        icon.paintIcon(null, g2, 0, 0)
        g2.dispose()
        return bufferedImage
    }
}