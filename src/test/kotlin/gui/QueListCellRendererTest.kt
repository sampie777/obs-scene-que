package gui

import objects.Globals
import objects.Que
import objects.TScene
import java.awt.Color
import javax.swing.JLabel
import javax.swing.JList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class QueListCellRendererTest {

    private val renderer = QueListCellRenderer()
    private val list: JList<TScene> = JList()

    @BeforeTest
    fun before() {
        Que.clear()
        Globals.scenes.clear()
        Globals.activeOBSSceneName = null
    }

    @Test
    fun testListWithNoCellSelected() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellSelected() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.selectedColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellActiveInOBS() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Globals.activeOBSSceneName = "1"
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeOBSColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellActiveInOBSAndSelected() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Globals.activeOBSSceneName = "1"
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeOBSSelectedColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueButNotActiveInOBS() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        Que.setCurrentSceneByIndex(0)
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeQueColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueButNotActiveInOBSAndSelected() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        Que.setCurrentSceneByIndex(0)
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeQueSelectedColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueAndActiveInOBS() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Globals.activeOBSSceneName = "1"
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        Que.setCurrentSceneByIndex(0)
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeQueAndOBSColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsCurrentQueAndActiveInOBSAndSelected() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Globals.activeOBSSceneName = "1"
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        Que.setCurrentSceneByIndex(0)
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.activeQueAndOBSSelectedColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsNonExistingScene() {
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(renderer.nonExistingColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testListWithFirstCellIsNonExistingSceneAndSelected() {
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, Que.getAt(0), 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.nonExistingSelectedColor, cell0.background)

        val cell1 = renderer.getListCellRendererComponent(list, Que.getAt(1), 1, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell1.background)

        val cell2 = renderer.getListCellRendererComponent(list, Que.getAt(2), 2, isSelected = false, cellHasFocus = false) as JLabel
        assertEquals(Color(255, 255, 255), cell2.background)
    }

    @Test
    fun testNullValueGivesDefaultBackgroundColor() {
        Globals.scenes.add(TScene("1"))
        Globals.scenes.add(TScene("2"))
        Globals.scenes.add(TScene("3"))
        Que.add(TScene("1"))
        Que.add(TScene("2"))
        Que.add(TScene("3"))
        list.setListData(Que.getList().toTypedArray())

        // When
        val cell0 = renderer.getListCellRendererComponent(list, null, 0, isSelected = true, cellHasFocus = false) as JLabel
        assertEquals(renderer.selectedColor, cell0.background)
    }
}