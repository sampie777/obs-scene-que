package gui

import gui.list.QueListCellRenderer
import mocks.MockPlugin
import mocks.QueItemMock
import objects.OBSState
import objects.que.Que
import objects.que.QueItem
import themes.Theme
import javax.swing.JLabel
import javax.swing.JList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("DEPRECATION")
class QueListCellRendererTest {
    
    private val mockPlugin = MockPlugin()
    private val renderer = QueListCellRenderer()
    private val list: JList<QueItem> = JList()

    @BeforeTest
    fun before() {
        Que.clear()
        OBSState.scenes.clear()
        OBSState.currentSceneName = null
    }


    @Test
    fun testRendererCallsQueItemsCellRenderer() {
        val queItem = mockPlugin.configStringToQueItem("1") as QueItemMock
        Que.add(queItem)
        list.setListData(Que.getList().toTypedArray())

        // When
        renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel

        assertTrue(queItem.cellRendererIsCalled)
    }

    @Test
    fun testListWithNoCellSelected() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_BACKGROUND_COLOR, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_BACKGROUND_COLOR, cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_BACKGROUND_COLOR, cell2.background)
    }

    @Test
    fun testListWithFirstCellSelected() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_SELECTION_BACKGROUND_COLOR, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_BACKGROUND_COLOR, cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_BACKGROUND_COLOR, cell2.background)
    }


    @Test
    fun testNullValueGivesDefaultBackgroundColor() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, null, 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(Theme.get.LIST_SELECTION_BACKGROUND_COLOR, cell0.background)
    }
}