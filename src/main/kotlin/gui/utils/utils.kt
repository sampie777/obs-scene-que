package gui.utils

import gui.mainFrame.MainFrame
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.net.URL
import java.util.logging.Logger
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneDivider

private val logger = Logger.getLogger("utils")


fun createGraphics(width: Int, height: Int): Pair<BufferedImage, Graphics2D> {
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g2: Graphics2D = bufferedImage.createGraphics()
    setDefaultRenderingHints(g2)
    return Pair(bufferedImage, g2)
}

fun setDefaultRenderingHints(g2: Graphics2D) {
    g2.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    )
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    )
    g2.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR
    )
}

fun drawImageInXYCenter(mainGraphics2D: Graphics2D, mainWidth: Int, mainHeight: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(
        bufferedImage,
        null,
        (mainWidth - bufferedImage.width) / 2,
        (mainHeight - bufferedImage.height) / 2
    )
}

fun drawImageInXCenter(mainGraphics2D: Graphics2D, y: Int, mainWidth: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(bufferedImage, null, (mainWidth - bufferedImage.width) / 2, y)
}

fun drawImageInYCenter(mainGraphics2D: Graphics2D, mainHeight: Int, x: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(bufferedImage, null, x, (mainHeight - bufferedImage.height) / 2)
}

fun getMainFrameComponent(childComponent: Component): JFrame? {
    return SwingUtilities.getAncestorOfClass(MainFrame::class.java, childComponent) as JFrame
}

fun loadIcon(iconPath: String): Image? {
    val resource: URL? = MainFrame::class.java.getResource(iconPath)
    if (resource == null) {
        logger.warning("Could not find icon: $iconPath")
        return null
    }

    return Toolkit.getDefaultToolkit().getImage(resource)
}

fun createImageIcon(path: String): ImageIcon? {
    val imgURL: URL? = MainFrame::class.java.getResource(path)
    if (imgURL != null) {
        return ImageIcon(imgURL)
    }

    logger.severe("Couldn't find imageIcon: $path")
    return null
}

fun isCtrlClick(modifiers: Int): Boolean = modifiers.and(ActionEvent.CTRL_MASK) != 0

fun getMainMenu(menu: JMenu) = (menu.popupMenu.invoker.parent as JPopupMenu).invoker

fun JSplitPane.divider(): BasicSplitPaneDivider = this.components.find { it is BasicSplitPaneDivider} as BasicSplitPaneDivider