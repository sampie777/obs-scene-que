package gui

import gui.list.QuePanel
import mocks.MockPlugin
import objects.OBSState
import objects.TScene
import objects.que.Que
import plugins.obs.ObsPlugin
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
}