package gui.globalHooks


import LogService
import config.Config
import objects.que.Que
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Logger


object GlobalKeyboardHook : NativeKeyListener {
    private val logger = Logger.getLogger(GlobalKeyboardHook::class.java.name)

    private var isCalibrating: Boolean = false
    private var calibrationCallback : ((NativeKeyEvent) -> Unit)? = null

    fun register() {
        // Unfortunately this must be done here and for some reason can't be done in LogService at boot
        LogService.setupLoggingForGlobalScreen()

        try {
            GlobalScreen.registerNativeHook()
        } catch (e: NativeHookException) {
            logger.warning("There was a problem registering the native hook")
            e.printStackTrace()
            return
        }

        GlobalScreen.addNativeKeyListener(GlobalKeyboardHook)
    }

    fun unregister() {
        try {
            logger.info("Unregistering native hook")
            GlobalScreen.unregisterNativeHook()
        } catch (t: Throwable) {
            logger.warning("There was a problem unregistering the native hook")
            t.printStackTrace()
            return
        }
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        processKeyEvent(e)
    }

    fun processKeyEvent(e: NativeKeyEvent) {
        // Remove Num lock, Scroll lock, and Caps lock
        e.modifiers = e.modifiers.and(NativeKeyEvent.NUM_LOCK_MASK - 1)

        if (isCalibrating) {
            processCalibration(e)
            return
        }

        if (Config.globalKeyEventNextQueueItem != null
            && Config.globalKeyEventNextQueueItem!!.isEqualTo(e)
        ) {
            logger.info("[NativeKeyEvent] Activate next Queue Item")
            Que.next()
        } else if (Config.globalKeyEventPreviousQueueItem != null
            && Config.globalKeyEventPreviousQueueItem!!.isEqualTo(e)
        ) {
            logger.info("[NativeKeyEvent] Activate previous Queue Item")
            Que.previous()
        }
    }

    private fun processCalibration(e: NativeKeyEvent) {
        isCalibrating = false

        if (calibrationCallback == null) {
            logger.warning("Cannot invoke calibrationCallback because callback is null")
            return
        }

        calibrationCallback?.invoke(e)
    }

    fun startCalibration(callback: (keyEvent: NativeKeyEvent) -> Unit) {
        calibrationCallback = callback
        isCalibrating = true
    }
}