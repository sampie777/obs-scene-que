package gui.quickAccessButtons

import config.Config
import mocks.MockPlugin
import mocks.QueItemMock
import themes.Theme
import java.awt.Color
import kotlin.test.*

@Suppress("DEPRECATION")
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
        assertEquals("""{"pluginName":"MockPlugin","className":"QueItemMock","name":"item1","executeAfterPrevious":false,"data":{}}""", Config.quickAccessButtonQueItems[0])
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
        assertEquals(Theme.get.BUTTON_BACKGROUND_COLOR, button.background)
    }

    @Test
    fun testSetNewItemWithColorGetsDisplayedWithColor() {
        Config.quickAccessButtonQueItems = ArrayList()
        Config.quickAccessButtonQueItems.add("")
        val button = QuickAccessButton(0, queItem = null)
        val queItem = plugin.configStringToQueItem("item1")
        queItem.quickAccessColor = Color(0, 100, 200)

        button.dropNewItem(queItem, 0)

        assertTrue(button.text.contains("item1"))
        assertTrue(button.isEnabled)
        assertEquals(Color(0, 100, 200), button.background)
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