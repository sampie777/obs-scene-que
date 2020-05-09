package gui

import config.Config
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class MainFrameTest {

    @Test
    fun testRestoreWindowSize() {
        Config.windowLocationX = 12
        Config.windowLocationY = 13
        Config.windowLocationWidth = 101
        Config.windowLocationHeight = 102
        Config.windowRestoreLastPosition = true

        val frame = MainFrame.create()

        assertEquals(12, frame.location.x)
        assertEquals(13, frame.location.y)
        assertEquals(101, frame.width)
        assertEquals(102, frame.height)
    }

    @Test
    fun testDoNotRestoreWindowSize() {
        Config.windowLocationX = 1112
        Config.windowLocationY = 1113
        Config.windowLocationWidth = 11101
        Config.windowLocationHeight = 11102
        Config.windowRestoreLastPosition = false

        val frame = MainFrame.create()

        assertNotEquals(1112, frame.location.x)
        assertNotEquals(1113, frame.location.y)
        assertEquals(1000, frame.width)
        assertEquals(600, frame.height)
    }

    @Test
    fun testSaveWindowPosition() {
        Config.windowLocationX = 0
        Config.windowLocationY = 0
        Config.windowLocationWidth = 0
        Config.windowLocationHeight = 0
        Config.windowRestoreLastPosition = false

        val frame = MainFrame.create()
        frame.setLocation(51, 52)
        frame.setSize(501, 502)

        frame.saveWindowPosition()
        assertEquals(51, Config.windowLocationX)
        assertEquals(52, Config.windowLocationY)
        assertEquals(501, Config.windowLocationWidth)
        assertEquals(502, Config.windowLocationHeight)
        assertFalse(Config.windowRestoreLastPosition)
    }
}