package plugins.easyworship

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC
import objects.notifications.Notifications
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent
import java.util.logging.Logger

class WindowFinder(private val windowTitle: String) : WNDENUMPROC {
    private val logger = Logger.getLogger(WindowFinder::class.java.name)

    var windowHandle: HWND? = null
    var count: Int = 0

    override fun callback(hWnd: HWND, arg1: Pointer?): Boolean {
        val windowText = CharArray(512)
        User32.INSTANCE.GetWindowText(hWnd, windowText, 512)
        val wText = Native.toString(windowText)

        // get rid of this if block if you want all windows regardless
        // of whether
        // or not they have text
        if (wText.isEmpty()) {
            return true
        }

        if (wText.contains(windowTitle)) {
            windowHandle = hWnd
            return false
        }
        return true
    }
}

/**
 * Handler to find an inner element of a window
 */
class InnerWindowFinder(private val innerWindowText: String) : WNDENUMPROC {
    private val logger = Logger.getLogger(InnerWindowFinder::class.java.name)

    var innerWindowHandle: HWND? = null
    var count: Int = 0

    override fun callback(hWnd: HWND, arg1: Pointer?): Boolean {
        val windowText = CharArray(512)
        User32.INSTANCE.GetWindowText(hWnd, windowText, 512)
        val wText = Native.toString(windowText)

        // get rid of this if block if you want all windows regardless
        // of whether
        // or not they have text
        if (wText.isEmpty()) {
            return true
        }

        if (wText.contains(innerWindowText)) {
            innerWindowHandle = hWnd
            return false
        }
        return true
    }
}

object EasyWorship {
    private val logger = Logger.getLogger(EasyWorship::class.java.name)

    private const val windowContainsText = "EasyWorship 2009"
    private const val innerWindowContainsText = "Genesis 1:1"

    private var windowHandle: HWND? = null
    private val robot = Robot()

    init {
        findWindowHandle(windowContainsText)
    }

    private fun findWindowHandle(windowTitle: String) {
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            logger.warning("Must run on Windows to find the window")
            Notifications.add("Application must run on Windows operating system", "EasyWorship")
            return
        }

        val windowFinder = WindowFinder(windowTitle)
        User32.INSTANCE.EnumWindows(windowFinder, null)
        windowHandle = windowFinder.windowHandle

        val innerWindowFinder = InnerWindowFinder(innerWindowContainsText)
        User32.INSTANCE.EnumChildWindows(windowHandle, innerWindowFinder, null)
        if (innerWindowFinder.innerWindowHandle != null) {
            windowHandle = innerWindowFinder.innerWindowHandle
        }

        if (windowHandle == null) {
            logger.warning("Failed to find window: $windowTitle")
            Notifications.add("Could not find '$windowTitle' window", "EasyWorship")
        }
    }

    fun doPreviousVerse(amount: Int = 1) {
        focus()

        for (step in 1..amount) {
            previousVerse()
        }
    }

    fun doNextVerse(amount: Int = 1) {
        focus()

        for (step in 1..amount) {
            nextVerse()
        }
    }

    fun doPreviousSong(amount: Int = 1) {
        focus()

        for (step in 1..amount) {
            previousSong()
        }
    }

    fun doNextSong(amount: Int = 1) {
        focus()

        for (step in 1..amount) {
            nextSong()
        }
    }

    fun doLogoScreen() {
        focus()
        logoScreen()
    }

    fun doBlackScreen() {
        focus()
        blackScreen()
    }

    fun doClearScreen() {
        focus()
        clearScreen()
    }

    private fun focus() {
        logger.info("Focus window")

        if (windowHandle == null) {
            findWindowHandle(windowContainsText)
        }

        if (windowHandle == null) {
            Notifications.add("Cannot focus window: window not found", "EasyWorship")
            return
        }

        User32.INSTANCE.SetForegroundWindow(windowHandle)
        User32.INSTANCE.SetFocus(windowHandle)
    }

    private fun previousVerse() {
        logger.info("Set previous verse")
        keyPress(EasyWorshipProperties.previousVerseKey)
    }

    private fun nextVerse() {
        logger.info("Set next verse")
        keyPress(EasyWorshipProperties.nextVerseKey)
    }

    private fun previousSong() {
        logger.info("Set previous song")
        keyPress(EasyWorshipProperties.previousSongKey)
    }

    private fun nextSong() {
        logger.info("Set next song")
        keyPress(EasyWorshipProperties.nextSongKey)
    }

    private fun logoScreen() {
        logger.info("Toggle logo screen")
        ctrlKeyPress(EasyWorshipProperties.logoScreenKey)
    }

    private fun blackScreen() {
        logger.info("Toggle black scree")
        ctrlKeyPress(EasyWorshipProperties.blackScreenKey)
    }

    private fun clearScreen() {
        logger.info("Toggle clear scree")
        ctrlKeyPress(EasyWorshipProperties.clearScreenKey)
    }

    private fun keyPress(key: Int, sleepTime: Int = 50) {
        try {
            robot.keyPress(key)
            robot.keyRelease(key)
            robot.delay(sleepTime)
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }

    private fun ctrlKeyPress(key: Int, sleepTime: Int = 50) {
        try {
            robot.keyPress(KeyEvent.VK_CONTROL)
            robot.keyPress(key)
            robot.keyRelease(key)
            robot.keyRelease(KeyEvent.VK_CONTROL)
            robot.delay(sleepTime)
        } catch (e: AWTException) {
            e.printStackTrace()
        }
    }
}