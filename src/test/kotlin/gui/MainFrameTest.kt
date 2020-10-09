package gui

import config.Config
import gui.mainFrame.MainFrame
import java.awt.Dimension
import java.awt.Point
import javax.swing.JFrame
import kotlin.test.*

class MainFrameTest {

    @BeforeTest
    fun before() {
        Config.mainWindowsIsMaximized = false
    }

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
        assertEquals(Dimension(1300, 760), frame.size)
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

    @Test
    fun testMaximizedWindowDoesntSaveSize() {
        Config.mainWindowLocation = Point(0, 0)
        Config.mainWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = true
        Config.mainWindowsIsMaximized = false
        val frame = MainFrame.create()
        frame.extendedState = JFrame.MAXIMIZED_BOTH
        frame.setLocation(8, 9)
        frame.setSize(10, 11)

        frame.saveWindowPosition()

        assertEquals(Point(8, 9), Config.mainWindowLocation)
        assertEquals(Dimension(0, 0), Config.mainWindowSize)
        assertTrue(Config.windowRestoreLastPosition)
        assertTrue(Config.mainWindowsIsMaximized)
    }

    @Test
    fun testConfigIsMaximizedLoadsAsMaximizedWindow() {
        Config.mainWindowLocation = Point(0, 0)
        Config.mainWindowSize = Dimension(0, 0)
        Config.windowRestoreLastPosition = true
        Config.mainWindowsIsMaximized = true

        val frame = MainFrame.create()

        assertEquals(Point(0, 0), frame.location)
        assertEquals(Dimension(0, 0), frame.size)
        assertEquals(JFrame.MAXIMIZED_BOTH, frame.extendedState)
    }

    @Test
    fun testConfigIsMaximizedLoadsAsMaximizedWindowButKeepsNotMaximizedSize() {
        Config.mainWindowSize = Dimension(100, 101)
        Config.windowRestoreLastPosition = true
        Config.mainWindowsIsMaximized = true

        val frame = MainFrame.create()

        assertEquals(Dimension(100, 101), frame.size)
        assertEquals(JFrame.MAXIMIZED_BOTH, frame.extendedState)
    }

}