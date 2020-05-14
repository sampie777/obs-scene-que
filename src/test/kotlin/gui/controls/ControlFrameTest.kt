package gui.controls

import config.Config
import org.junit.Test
import java.awt.Dimension
import java.awt.Point
import javax.swing.JFrame
import kotlin.test.*

class ControlFrameTest {

    @BeforeTest
    fun before() {
        Config.controlWindowsIsMaximized = false
    }

    @Test
    fun testRestoreWindowSize() {
        Config.controlWindowLocation = Point(12, 13)
        Config.controlWindowSize = Dimension(101, 102)
        Config.windowRestoreLastPosition = true

        val frame = ControlFrame.create(null)

        assertEquals(Point(12, 13), frame.location)
        assertEquals(Dimension(101, 102), frame.size)
    }

    @Test
    fun testDoNotRestoreWindowSize() {
        Config.controlWindowLocation = Point(1112, 1113)
        Config.controlWindowSize = Dimension(11101, 11102)
        Config.windowRestoreLastPosition = false

        val frame = ControlFrame.create(null)

        assertNotEquals(1112, frame.location.x)
        assertNotEquals(1113, frame.location.y)
        assertEquals(Dimension(500, 250), frame.size)
    }

    @Test
    fun testSaveWindowPosition() {
        Config.controlWindowLocation = Point(0, 0)
        Config.controlWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = false
        val frame = ControlFrame.create(null)
        frame.setLocation(51, 52)
        frame.setSize(501, 502)

        frame.saveWindowPosition()

        assertEquals(Point(51, 52), Config.controlWindowLocation)
        assertEquals(Dimension(501, 502), Config.controlWindowSize)
        assertFalse(Config.windowRestoreLastPosition)
    }

    @Test
    fun testMaximizedWindowDoesntSaveSize() {
        Config.controlWindowLocation = Point(0, 0)
        Config.controlWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = true
        Config.controlWindowsIsMaximized = false
        val frame = ControlFrame.create(null)
        frame.extendedState = JFrame.MAXIMIZED_BOTH
        frame.setLocation(8, 9)
        frame.setSize(10, 11)

        frame.saveWindowPosition()

        assertEquals(Point(8, 9), Config.controlWindowLocation)
        assertEquals(Dimension(0, 0), Config.controlWindowSize)
        assertTrue(Config.windowRestoreLastPosition)
        assertTrue(Config.controlWindowsIsMaximized)
    }

    @Test
    fun testConfigIsMaximizedLoadsAsMaximizedWindow() {
        Config.controlWindowLocation = Point(0, 0)
        Config.controlWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = true
        Config.controlWindowsIsMaximized = true

        val frame = ControlFrame.create(null)

        assertEquals(Point(0, 0), frame.location)
        assertEquals(Dimension(0, 0), frame.size)
        assertEquals(JFrame.MAXIMIZED_BOTH, frame.extendedState)
    }

    @Test
    fun testConfigIsMaximizedLoadsAsMaximizedWindowButKeepsNotMaximizedSize() {
        Config.controlWindowSize = Dimension(100, 101)
        Config.windowRestoreLastPosition = true
        Config.controlWindowsIsMaximized = true

        val frame = ControlFrame.create(null)

        assertEquals(Dimension(100, 101), frame.size)
        assertEquals(JFrame.MAXIMIZED_BOTH, frame.extendedState)
    }

}