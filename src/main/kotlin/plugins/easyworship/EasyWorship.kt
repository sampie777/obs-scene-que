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
        print("To string: ")
        println(windowText.toString())
        val wText = Native.toString(windowText)

        // get rid of this if block if you want all windows regardless
        // of whether
        // or not they have text
        if (wText.isEmpty()) {
            return true
        }

        logger.fine(wText)
        println(wText)

        if (wText.contains(windowTitle)) {
            windowHandle = hWnd
            return false
        }
        return true
    }
}

object EasyWorship {
    private val logger = Logger.getLogger(EasyWorship::class.java.name)

    private const val windowContainsText = "EasyWorship 2009"
    private const val previousVerseKey = KeyEvent.VK_UP
    private const val nextVerseKey = KeyEvent.VK_DOWN
    private const val previousSongKey = KeyEvent.VK_PAGE_UP
    private const val nextSongKey = KeyEvent.VK_PAGE_DOWN
    private const val logoScreenKey = KeyEvent.VK_L
    private const val blackScreenKey = KeyEvent.VK_B
    private const val clearScreenKey = KeyEvent.VK_C

    private var windowHandle: HWND? = null
    private val robot = Robot()

    init {
        findWindowHandle(windowContainsText)
    }

    private fun findWindowHandle(windowTitle: String) {
        val windowFinder = WindowFinder(windowTitle)
        User32.INSTANCE.EnumWindows(windowFinder, null)

        windowHandle = windowFinder.windowHandle

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

        User32.INSTANCE.SetForegroundWindow(windowHandle)
        User32.INSTANCE.SetFocus(windowHandle)
    }

    private fun previousVerse() {
        logger.info("Set previous verse")
        keyPress(previousVerseKey)
    }

    private fun nextVerse() {
        logger.info("Set next verse")
        keyPress(nextVerseKey)
    }

    private fun previousSong() {
        logger.info("Set previous song")
        keyPress(previousSongKey)
    }

    private fun nextSong() {
        logger.info("Set next song")
        keyPress(nextSongKey)
    }

    private fun logoScreen() {
        logger.info("Toggle logo screen")
        ctrlKeyPress(logoScreenKey)
    }

    private fun blackScreen() {
        logger.info("Toggle black scree")
        ctrlKeyPress(blackScreenKey)
    }

    private fun clearScreen() {
        logger.info("Toggle clear scree")
        ctrlKeyPress(clearScreenKey)
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