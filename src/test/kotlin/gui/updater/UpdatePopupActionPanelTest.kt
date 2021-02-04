package gui.updater

import config.Config
import javax.swing.JButton
import javax.swing.JCheckBox
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class UpdatePopupActionPanelTest {

    @Test
    fun `test update checkbox contains the right text and is the right selection`() {
        val frame = UpdatePopup.create("v1.2.3", null)
        val contentPanel = UpdatePopupActionPanel(frame)

        val disableUpdateCheckerCheckbox = contentPanel.components[0] as JCheckBox
        assertEquals("Don't check for future updates", disableUpdateCheckerCheckbox.text)
        assertEquals(Config.updatesCheckForUpdates, !disableUpdateCheckerCheckbox.isSelected)
    }

    @Test
    fun `test change in update selectbox will be saved to config on close action`() {
        Config.updatesCheckForUpdates = true
        val frame = UpdatePopup.create("v1.2.3", null)
        val contentPanel = UpdatePopupActionPanel(frame)

        val disableUpdateCheckerCheckbox = contentPanel.components[0] as JCheckBox
        val closeButton = contentPanel.components[2] as JButton

        assertEquals("Don't check for future updates", disableUpdateCheckerCheckbox.text)
        assertFalse(disableUpdateCheckerCheckbox.isSelected)

        // When
        closeButton.doClick()

        // Then
        assertTrue(Config.updatesCheckForUpdates)
    }
}