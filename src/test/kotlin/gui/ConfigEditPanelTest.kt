package gui

import config.Config
import gui.config.ConfigEditPanel
import objects.notifications.Notifications
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigEditPanelTest {

    @Test
    fun testSaveAllValid() {
        Config.obsAddress = "someaddress"
        val panel = ConfigEditPanel()

        Config.obsAddress = ""
        assertTrue(panel.saveAll())
        assertEquals("someaddress", Config.obsAddress)
    }

    @Test
    fun testSaveAllInvalid() {
        Config.obsAddress = ""
        val panel = ConfigEditPanel()

        Config.obsAddress = "someaddress"
        assertFalse(panel.saveAll())
        assertEquals("someaddress", Config.obsAddress)
    }

    @Test
    fun testComponentWithInvalidValuesWillGiveErrorNotification() {
        Notifications.clear()
        // Set invalid config value
        Config.set("timerCountUpFontSize", -9000)

        ConfigEditPanel()

        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(
            Notifications.list[0].message.contains("timerCountUpFontSize"),
            "Notifications doesn't containt the proper message"
        )
    }
}