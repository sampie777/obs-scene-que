package gui.controls

import GUI
import gui.mainFrame.MainFrame
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ControlFramePanelTest {

    @BeforeTest
    fun before() {
        GUI.unregisterAll()
    }

    @Test
    fun testWindowCloseWithValidParentUnregistersAtGUI() {
        val panel = ControlFramePanel()
        assertTrue(GUI.isRegistered(panel))

        panel.windowClosing(ControlFrame.create(null))

        assertFalse(GUI.isRegistered(panel))
    }

    @Test
    fun testWindowCloseWithInvalidParentDoesntUnregisterAtGUI() {
        val panel = ControlFramePanel()
        assertTrue(GUI.isRegistered(panel))

        panel.windowClosing(MainFrame.create())

        assertTrue(GUI.isRegistered(panel))
    }
}