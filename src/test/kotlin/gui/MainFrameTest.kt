package gui

import config.Config
import org.junit.Test
import java.awt.Dimension
import java.awt.Point
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class MainFrameTest {

    @Test
    fun testRestoreWindowSize() {
        Config.mainWindowLocation = Point(12, 13)
        Config.mainWindowSize = Dimension(101, 102)
        Config.windowRestoreLastPosition = true

        val frame = MainFrame.create()

        assertEquals(Point(12, 13), frame.location)
        assertEquals(Dimension(101, 102), frame.size)
    }

    @Test
    fun testDoNotRestoreWindowSize() {
        Config.mainWindowLocation = Point(1112, 1113)
        Config.mainWindowSize = Dimension(11101, 11102)
        Config.windowRestoreLastPosition = false

        val frame = MainFrame.create()

        assertNotEquals(1112, frame.location.x)
        assertNotEquals(1113, frame.location.y)
        assertEquals(Dimension(1000, 600), frame.size)
    }

    @Test
    fun testSaveWindowPosition() {
        Config.mainWindowLocation = Point(0, 0)
        Config.mainWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = false

        val frame = MainFrame.create()
        frame.setLocation(51, 52)
        frame.setSize(501, 502)

        frame.saveWindowPosition()
        assertEquals(Point(51, 52), Config.mainWindowLocation)
        assertEquals(Dimension(501, 502), Config.mainWindowSize)
        assertFalse(Config.windowRestoreLastPosition)
    }
}