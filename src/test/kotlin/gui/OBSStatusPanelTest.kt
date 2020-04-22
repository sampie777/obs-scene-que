package gui

import config.Config
import objects.Globals
import objects.OBSStatus
import kotlin.test.*

class OBSStatusPanelTest {

    @BeforeTest
    fun before() {
        Config.obsAddress = "ws://localhost:4444"
        Globals.OBSActivityStatus = null
        Globals.OBSConnectionStatus = OBSStatus.UNKNOWN
    }

    @Test
    fun testGetOBSStatusRepresentationWithUnkownStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSConnectionStatus = OBSStatus.UNKNOWN

        assertEquals("Unknown", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testGetOBSStatusRepresentationWithConnectingStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSConnectionStatus = OBSStatus.CONNECTING

        assertEquals("Connecting to ${Config.obsAddress}...", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testGetOBSStatusRepresentationWithConnectedStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSConnectionStatus = OBSStatus.CONNECTED

        assertEquals("Connected", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testGetOBSStatusRepresentationWithDisconnectedStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSConnectionStatus = OBSStatus.DISCONNECTED

        assertEquals("Disconnected", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testGetOBSStatusRepresentationWithConnectionFailedStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSConnectionStatus = OBSStatus.CONNECTION_FAILED

        assertEquals("Connection failed!", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testGetOBSStatusRepresentationWithLoadingScenesStatus() {
        val panel = OBSStatusPanel()

        Globals.OBSActivityStatus = OBSStatus.LOADING_SCENES
        Globals.OBSConnectionStatus = OBSStatus.CONNECTED

        assertEquals("Loading scenes...", panel.getOBSStatusRepresentation())
    }

    @Test
    fun testMessageLabelWithRefreshingOBSStatus() {
        Globals.OBSConnectionStatus = OBSStatus.UNKNOWN
        val panel = OBSStatusPanel()

        assertEquals("OBS: Unknown", panel.getMessageLabel().text)
        assertFalse(panel.getMessageLabel().toolTipText.contains("Connected"),
            "'Connected' string is falsy in messageLabel tooltip text")

        Globals.OBSConnectionStatus = OBSStatus.CONNECTED
        panel.refreshOBSStatus()

        assertEquals("OBS: Connected", panel.getMessageLabel().text)
        assertTrue(panel.getMessageLabel().toolTipText.contains("Connected"),
            "'Connected' string is missing in messageLabel tooltip text")
    }
}