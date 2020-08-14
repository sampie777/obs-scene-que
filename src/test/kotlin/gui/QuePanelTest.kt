package gui

import gui.list.QuePanel
import mocks.MockPlugin
import mocks.QueItemMock
import objects.OBSState
import objects.TScene
import objects.que.Que
import plugins.obs.ObsPlugin
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.test.*

@Suppress("DEPRECATION")
class QuePanelTest {
    
    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
        OBSState.scenes.clear()
    }

    @Test
    fun testRemoveItemButtonRemovesSelectedItem() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))

        val panel = QuePanel()
        panel.list.selectedIndex = 1

        // When
        panel.removeItemButton.doClick()

        assertEquals(2, Que.size())
        assertEquals("1", Que.getList()[0].name)
        assertEquals("3", Que.getList()[1].name)
    }

    @Test
    fun testRemoveItemButtonRemovesNothingWhenNothingSelected() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))

        val panel = QuePanel()
        panel.list.selectedIndex = -1

        // When
        panel.removeItemButton.doClick()

        assertEquals(3, Que.size())
        assertEquals("1", Que.getList()[0].name)
        assertEquals("2", Que.getList()[1].name)
        assertEquals("3", Que.getList()[2].name)
    }

    @Test
    fun testRemoveAllButtonClearsQue() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))

        val panel = QuePanel()

        // When
        panel.removeAllButton.doClick()

        assertEquals(0, Que.size())
    }

    @Test
    fun testRemoveInvalidObsSceneItemsButtonRemovesInvalidItems() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        OBSState.scenes.add(TScene("3"))
        val obsPlugin = ObsPlugin()
        Que.add(obsPlugin.configStringToQueItem("1"))
        Que.add(obsPlugin.configStringToQueItem("invalid 1"))
        Que.add(obsPlugin.configStringToQueItem("3"))
        Que.add(obsPlugin.configStringToQueItem("invalid 2"))

        val panel = QuePanel()

        // When
        panel.removeInvalidItemsButton.doClick()

        assertEquals(2, Que.size())
        assertEquals("1", Que.getList()[0].name)
        assertEquals("3", Que.getList()[1].name)
    }

    @Test
    fun testSwitchedScenesClearsSelection() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        val panel = QuePanel()
        assertEquals(3, panel.list.model.size)
        panel.list.selectedIndex = 1

        // When
        panel.switchedScenes()

        assertEquals(-1, panel.list.selectedIndex)
    }

    @Test
    fun testRefreshQueScenesLoadsNewQueIntoList() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        val panel = QuePanel()
        assertEquals(1, panel.list.model.size)

        // When
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        panel.refreshQueItems()

        assertEquals(3, panel.list.model.size)

        // When
        Que.clear()
        panel.refreshQueItems()

        assertEquals(0, panel.list.model.size)
    }

    @Test
    fun testDoubleClickItemActivatesItem() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)
        val panel = QuePanel()

        panel.list.selectedIndex = 1
        val e = MouseEvent(panel.list, 0, 0, MouseEvent.BUTTON1, 0, 0, 2, false)
        panel.list.mouseListeners.forEach { it.mouseClicked(e) }

        assertTrue(item1.isDeactivated)
        assertTrue(item2.isActivated)
        assertEquals(item2, Que.current())
    }

    @Test
    fun testCtrlClickItemSetsCurrentItemWithoutActivatingIt() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)
        val panel = QuePanel()

        panel.list.selectedIndex = 1
        val e = MouseEvent(panel.list, 0, 0, MouseEvent.BUTTON1.or(ActionEvent.CTRL_MASK), 0, 0, 1, false)
        panel.list.mouseListeners.forEach { it.mouseClicked(e) }

        assertTrue(item1.isDeactivated)
        assertFalse(item2.isActivated)
        assertEquals(item2, Que.current())
    }

    @Test
    fun testEnterKeyPressActivatesItem() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)
        val panel = QuePanel()

        panel.list.selectedIndex = 1
        val e = KeyEvent(panel.list, 0, 0, 0, KeyEvent.VK_ENTER)
        panel.list.keyListeners.forEach { it.keyReleased(e) }

        assertTrue(item1.isDeactivated)
        assertTrue(item2.isActivated)
        assertEquals(item2, Que.current())
    }
}