package gui.utils


import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.Icon
import javax.swing.JLabel

class IconLabel(private val icon: Icon?, private val text: String?) : Box(0) {
    private val logger = Logger.getLogger(IconLabel::class.java.name)

    init {
        add(JLabel(icon))
        add(createRigidArea(Dimension(5, 0)))
        add(JLabel(text))
    }
}