package gui.updater

import javax.swing.JPanel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class UpdatePopupTest {

    @Test
    fun `test frame content contains version and content panel`() {
        val dialog = UpdatePopup.create("v1.2.3", null)

        assertEquals("New update available", dialog.title)

        val components = (dialog.contentPane.components[0] as JPanel).components
        assertEquals(2, components.size)

        val contentPanel = components[0]
        assertTrue(contentPanel is UpdatePopupContent)
        assertEquals("v1.2.3", contentPanel.version)

        assertTrue(components[1] is UpdatePopupActionPanel)
    }
}