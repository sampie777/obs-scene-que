package gui.list

import GUI
import objects.que.Que
import objects.que.QueItem
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import java.util.logging.Logger
import javax.swing.AbstractAction
import javax.swing.JList
import javax.swing.KeyStroke

class QueListKeyListener(private val panel: QuePanel, private val list: JList<QueItem>) : KeyListener {

    private val logger = Logger.getLogger(QueListKeyListener::class.java.name)

    private val keyEvents = HashMap<Int, (e: KeyEvent) -> Unit>()

    init {
        addKeyStrokeEvent(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK)) {
            logger.info("[KeyEvent] Shift + Left pressed")
            val item: QueItem = list.selectedValue ?: return@addKeyStrokeEvent
            item.executeAfterPrevious = false
            GUI.refreshQueItems()
        }

        addKeyStrokeEvent(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK)) {
            logger.info("[KeyEvent] Shift + Right pressed")
            val item: QueItem = list.selectedValue ?: return@addKeyStrokeEvent
            item.executeAfterPrevious = true
            GUI.refreshQueItems()
        }

        keyEvents[KeyEvent.VK_ENTER] = {
            logger.info("[KeyEvent] Enter key pressed")
            panel.activateSelectedIndex(list.selectedIndex)
        }

        keyEvents[KeyEvent.VK_DELETE] = {
            logger.info("[KeyEvent] Delete key pressed")
            Que.remove(list.selectedIndex)
            GUI.refreshQueItems()
        }
    }

    private fun addKeyStrokeEvent(keyStroke: KeyStroke, callback: (e: ActionEvent) -> Unit) {
        val identifier = UUID.randomUUID()
        list.inputMap.put(keyStroke, identifier)
        list.actionMap.put(identifier, object: AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                try {
                    callback(e)
                } catch (e: Exception) {
                    logger.warning("Failed to execute keystroke: $keyStroke")
                    e.printStackTrace()
                }
            }
        })
    }

    override fun keyTyped(keyEvent: KeyEvent) {}

    override fun keyPressed(keyEvent: KeyEvent) {}

    override fun keyReleased(keyEvent: KeyEvent) {
        if (list.selectedIndex < 0 || list.selectedIndex >= Que.size()) {
            return
        }

        val callback = keyEvents[keyEvent.keyCode] ?: return
        try {
            callback.invoke(keyEvent)
        } catch (e: Exception) {
            logger.warning("Failed to execute keystroke: ${keyEvent.keyCode}")
            e.printStackTrace()
        }
    }
}
