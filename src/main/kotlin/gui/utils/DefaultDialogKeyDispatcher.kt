package gui.utils

import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.Window
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.*
import java.util.logging.Logger
import javax.swing.KeyStroke

class DefaultDialogKeyDispatcher(private val frame: Window) : KeyEventDispatcher {

    private val logger = Logger.getLogger(DefaultDialogKeyDispatcher::class.java.name)

    private val keyEvents = HashMap<Int, (e: KeyEvent) -> Unit>()
    private val keyStrokes = HashMap<KeyStroke, (e: KeyEvent) -> Unit>()

    init {
        keyEvents[KeyEvent.VK_ESCAPE] = {
            closeWindow()
        }

        keyStrokes[KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK)] = {
            closeWindow()
        }
    }

    private fun closeWindow() {
        frame.dispose()
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .removeKeyEventDispatcher(this)
    }

    override fun dispatchKeyEvent(keyEvent: KeyEvent): Boolean {
        if (keyEvent.id != KeyEvent.KEY_PRESSED) {
            return false
        }

        val callback = if (keyEvent.modifiers == 0) {
            keyEvents[keyEvent.keyCode]
        } else {
            getKeyStroke(keyEvent)
        }

        if (callback == null) {
            return false
        }

        logger.info("Executing keystroke for keyevent: $keyEvent")

        try {
            callback.invoke(keyEvent)
        } catch (e: Exception) {
            logger.warning("Failed to execute keystroke: ${keyEvent.keyCode}")
            e.printStackTrace()
        } finally {
            return false
        }
    }

    private fun getKeyStroke(keyEvent: KeyEvent): ((KeyEvent) -> Unit)? {
        val key = keyStrokes.keys
            .find {
                // Mask modifier with 63 (in bits) to discard all higher up key DOWN_MASKS,
                // because the normal MASK is sufficient
                it.keyCode == keyEvent.keyCode && it.modifiers.and(63) == keyEvent.modifiers
            } ?: return null
        return keyStrokes[key]
    }
}