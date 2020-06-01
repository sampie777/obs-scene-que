package objects.que

import mocks.MockPlugin
import mocks.MockPlugin2
import mocks.QueItemMock
import mocks.QueItemMock2
import kotlin.test.*

@Suppress("DEPRECATION")
class QueTest {
    
    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
    }

    @Test
    fun testAddAtSpecificPoint() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        val item = mockPlugin.configStringToQueItem("insert")

        Que.add(1, item)

        assertEquals(4, Que.size())
        assertEquals(item, Que.getAt(1))
    }

    @Test
    fun testAddDoubleItemsFromSamePlugin() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("double")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        val item = mockPlugin.configStringToQueItem("double")

        Que.add(2, item)

        assertEquals(4, Que.size())
    }

    @Test
    fun testAddDoubleItemsFromDifferentPlugin() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("double")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        val item = MockPlugin2().configStringToQueItem("double")

        Que.add(2, item)

        assertEquals(4, Que.size())
    }

    @Test
    fun testNext() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        assertEquals(-1, Que.currentIndex())
        assertNull(Que.current())

        Que.next()
        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())

        Que.next()
        assertEquals(1, Que.currentIndex())
        assertEquals(item2, Que.current())

        Que.next()
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())

        Que.next()
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())
    }

    @Test
    fun testPrevious() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        Que.setCurrentQueItemByIndex(2)
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())

        Que.previous()
        assertEquals(1, Que.currentIndex())
        assertEquals(item2, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())
    }

    @Test
    fun testNextWithExecuteAfterPreviousItems() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        item2.executeAfterPrevious = true
        val item3 = mockPlugin.configStringToQueItem("3") as QueItemMock
        item3.executeAfterPrevious = true
        val item4 = mockPlugin.configStringToQueItem("4") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)
        Que.add(item4)

        assertEquals(-1, Que.currentIndex())
        assertNull(Que.current())

        Que.next()
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())
        assertTrue(item1.isActivated)
        assertTrue(item2.isActivated)
        assertTrue(item3.isActivated)
        assertFalse(item4.isActivated)

        Que.next()
        assertEquals(3, Que.currentIndex())
        assertEquals(item4, Que.current())
        assertTrue(item4.isActivated)
    }

    @Test
    fun testPreviousWithExecuteAfterPreviousItems() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        item2.executeAfterPrevious = true
        val item3 = mockPlugin.configStringToQueItem("3") as QueItemMock
        item3.executeAfterPrevious = true
        val item4 = mockPlugin.configStringToQueItem("4") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)
        Que.add(item4)

        Que.setCurrentQueItemByIndex(3)
        assertEquals(3, Que.currentIndex())
        assertEquals(item4, Que.current())

        Que.previous()
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())

        Que.previous()
        assertEquals(1, Que.currentIndex())
        assertEquals(item2, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())
    }

    @Test
    fun testGetAt() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        assertNull(Que.getAt( -1))
        assertNull(Que.getAt( 5))
        assertEquals(item2, Que.getAt(1))
    }

    @Test
    fun testSetCurrentItemByIndex() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        Que.setCurrentQueItemByIndex(1)
        assertEquals(1, Que.currentIndex())
        assertEquals(item2, Que.current())
    }

    @Test
    fun testSetCurrentItemByName() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = MockPlugin2().configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("2")
        val item4 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)
        Que.add(item4)

        Que.setCurrentQueItemByName(mockPlugin.name, "2")
        assertEquals(2, Que.currentIndex())
        assertEquals(item3, Que.current())
    }

    @Test
    fun testMove() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        assertTrue(Que.move(0, 1))  // Nothing happens
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertTrue(Que.move(0, 2))
        assertEquals(item2, Que.getAt(0))
        assertEquals(item1, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertTrue(Que.move(1, 0))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertTrue(Que.move(0, 3))
        assertEquals(item2, Que.getAt(0))
        assertEquals(item3, Que.getAt(1))
        assertEquals(item1, Que.getAt(2))

        assertTrue(Que.move(2, 0))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertFalse(Que.move(0, 4))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertFalse(Que.move(-1, 2))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertFalse(Que.move(4, 0))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))

        assertFalse(Que.move(0, -1))
        assertEquals(item1, Que.getAt(0))
        assertEquals(item2, Que.getAt(1))
        assertEquals(item3, Que.getAt(2))
    }

    @Test
    fun testActivateCurrentQueItem() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        Que.add(item1)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isActivated)

        Que.activateCurrent()

        assertTrue(item1.isActivated)
    }

    @Test
    fun testNextQueItemGetsActivatedOnNextCall() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)

        Que.next()

        assertTrue(item1.isDeactivated)
        assertTrue(item2.isActivated)
    }

    @Test
    fun testPreviousQueItemGetsActivatedOnPreviousCall() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(1)
        assertFalse(item1.isActivated)
        assertFalse(item2.isDeactivated)

        Que.previous()

        assertTrue(item1.isActivated)
        assertTrue(item2.isDeactivated)
    }

    @Test
    fun testNextQueItemDoesntGetsActivatedOnPreviewNextCall() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(0)
        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)

        Que.previewNext()

        assertFalse(item1.isDeactivated)
        assertFalse(item2.isActivated)
    }

    @Test
    fun testPreviousQueItemDoesntGetsActivatedOnPreviewPreviousCall() {
        val item1 = mockPlugin.configStringToQueItem("1") as QueItemMock
        val item2 = mockPlugin.configStringToQueItem("2") as QueItemMock
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(1)
        assertFalse(item1.isActivated)
        assertFalse(item2.isDeactivated)

        Que.previewPrevious()

        assertFalse(item1.isActivated)
        assertFalse(item2.isDeactivated)
    }

    @Test
    fun testActivatePreviousItemAsPrevious() {
        val item1 = QueItemMock2(mockPlugin, "1")
        val item2 = QueItemMock2(mockPlugin, "2")
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(1)

        Que.previous()
        assertTrue(item1.isActivatedAsPrevious)
        assertFalse(item1.isActivated)
        assertFalse(item2.isActivatedAsPrevious)
        assertFalse(item2.isActivated)
    }

    @Test
    fun testActivatePreviousItemAsPreviousWithDefaultMethod() {
        val item1 = QueItemMock(mockPlugin, "1")
        val item2 = QueItemMock(mockPlugin, "2")
        Que.add(item1)
        Que.add(item2)
        Que.setCurrentQueItemByIndex(1)

        Que.previous()
        assertTrue(item1.isActivated)
        assertFalse(item2.isActivated)
    }
}