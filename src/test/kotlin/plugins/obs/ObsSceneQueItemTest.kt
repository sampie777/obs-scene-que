package plugins.obs

import objects.OBSState
import objects.TScene
import objects.que.Que
import themes.Theme
import javax.swing.JLabel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObsSceneQueItemTest {

    private val plugin = ObsPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
        OBSState.scenes.clear()
        OBSState.currentSceneName = null
    }

    @Test
    fun testListWithFirstCellActiveInOBS() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        OBSState.currentSceneName = "1"
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = false, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_OBS_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellActiveInOBSAndSelected() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        OBSState.currentSceneName = "1"
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = true, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_OBS_SELECTED_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueButNotActiveInOBS() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        Que.setCurrentQueItemByIndex(0)
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = false, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_QUE_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueButNotActiveInOBSAndSelected() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        Que.setCurrentQueItemByIndex(0)
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = true, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_QUE_SELECTED_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueAndActiveInOBS() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        OBSState.currentSceneName = "1"
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        Que.setCurrentQueItemByIndex(0)
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = false, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_QUE_AND_OBS_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueAndActiveInOBSAndSelected() {
        OBSState.scenes.add(TScene("1"))
        OBSState.scenes.add(TScene("2"))
        OBSState.currentSceneName = "1"
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        Que.setCurrentQueItemByIndex(0)
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = true, cellHasFocus = false)
        assertEquals(Theme.get.ACTIVE_QUE_AND_OBS_SELECTED_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsNonExistingScene() {
        OBSState.scenes.add(TScene("2"))
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = false, cellHasFocus = false)
        assertEquals(Theme.get.NON_EXISTING_COLOR, cell.background)
    }

    @Test
    fun testListWithFirstCellIsNonExistingSceneAndSelected() {
        OBSState.scenes.add(TScene("2"))
        val queItem = plugin.configStringToQueItem("1")
        Que.add(queItem)
        Que.add(plugin.configStringToQueItem("2"))
        val cell = JLabel()

        // When
        queItem.getListCellRendererComponent(cell, 0, isSelected = true, cellHasFocus = false)
        assertEquals(Theme.get.NON_EXISTING_SELECTED_COLOR, cell.background)
    }
}