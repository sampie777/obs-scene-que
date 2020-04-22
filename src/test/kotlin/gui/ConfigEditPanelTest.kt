package gui

import config.Config
import gui.config.ConfigEditPanel
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
}