package gui

import objects.Globals
import objects.Que
import objects.TScene
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SceneQuePanelTest {

    @BeforeTest
    fun before() {
        Que.clear()
        Globals.scenes.clear()
    }

    @Test
    fun testRemoveItemButtonRemovesSelectedItem() {
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))

        val panel = SceneQuePanel()
        panel.list.selectedIndex = 1

        // When
        panel.removeItemButton.doClick()

        assertEquals(2, Que.size())
        assertEquals("1", Que.getList()[0].name)
        assertEquals("3", Que.getList()[1].name)
    }

    @Test
    fun testRemoveItemButtonRemovesNothingWhenNothingSelected() {
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))

        val panel = SceneQuePanel()
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
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))

        val panel = SceneQuePanel()

        // When
        panel.removeAllButton.doClick()

        assertEquals(0, Que.size())
    }

    @Test
    fun testRemoveInvalidItemsButtonRemovesInvalidItems() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("invalid 1"))
        Que.add(TScene("3"))
        Que.add(TScene("invalid 2"))

        val panel = SceneQuePanel()

        // When
        panel.removeInvalidItemsButton.doClick()

        assertEquals(2, Que.size())
        assertEquals("1", Que.getList()[0].name)
        assertEquals("3", Que.getList()[1].name)
    }

    @Test
    fun testSwitchedScenesClearsSelection() {
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        val panel = SceneQuePanel()
        assertEquals(3, panel.list.model.size)
        panel.list.selectedIndex = 1

        // When
        panel.switchedScenes()

        assertEquals(-1, panel.list.selectedIndex)
    }

    @Test
    fun testRefreshQueScenesLoadsNewQueIntoList() {
        Que.add(TScene("1"))
        val panel = SceneQuePanel()
        assertEquals(1, panel.list.model.size)

        // When
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        panel.refreshQueScenes()

        assertEquals(3, panel.list.model.size)

        // When
        Que.clear()
        panel.refreshQueScenes()

        assertEquals(0, panel.list.model.size)
    }
}