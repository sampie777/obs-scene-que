package gui.quickAccessButtons

import config.Config
import mocks.MockPlugin
import mocks.QueItemMock
import kotlin.test.*

class QuickAccessButtonTest {

    private val plugin = MockPlugin()

    @Test
    fun testSetNewItemSavesToConfig() {
        Config.quickAccessButtonQueItems = ArrayList()
        Config.quickAccessButtonQueItems.add("")
        val button = QuickAccessButton(0, queItem = null)
        val queItem = plugin.configStringToQueItem("item1")

        button.dropNewItem(queItem, 0)

        assertEquals(queItem, button.getQueItem())
        assertEquals("MockPlugin|false|item1", Config.quickAccessButtonQueItems[0])
    }

    @Test
    fun testSetNewEmptyItemSavesToConfig() {
        Config.quickAccessButtonQueItems = ArrayList()
        Config.quickAccessButtonQueItems.add("MockPlugin|false|item1")
        val queItem = plugin.configStringToQueItem("item1")
        val button = QuickAccessButton(0, queItem = queItem)

        button.clearItem()

        assertNull(button.getQueItem())
        assertEquals("", Config.quickAccessButtonQueItems[0])
    }

    @Test
    fun testSetNewItemGetsDisplayed() {
        Config.quickAccessButtonQueItems = ArrayList()
        Config.quickAccessButtonQueItems.add("")
        val button = QuickAccessButton(0, queItem = null)
        val queItem = plugin.configStringToQueItem("item1")

        button.dropNewItem(queItem, 0)

        assertTrue(button.text.contains("item1"))
        assertTrue(button.isEnabled)
    }

    @Test
    fun testSetNewEmptyItemGetsDisplayed() {
        Config.quickAccessButtonQueItems = ArrayList()
        Config.quickAccessButtonQueItems.add("MockPlugin|false|item1")
        val queItem = plugin.configStringToQueItem("item1")
        val button = QuickAccessButton(0, queItem = queItem)

        button.clearItem()

        assertTrue(button.text.contains("empty"))
        assertFalse(button.isEnabled)
    }

    @Test
    fun testItemGetsActivatedOnClick() {
        val queItem = plugin.configStringToQueItem("item1") as QueItemMock
        val button = QuickAccessButton(0, queItem = queItem)

        button.doClick()

        assertTrue(queItem.isActivated)
    }

    @Test
    fun testEmptyItemDoesNothingOnClick() {
        val button = QuickAccessButton(0, queItem = null)

        button.doClick()
    }
}