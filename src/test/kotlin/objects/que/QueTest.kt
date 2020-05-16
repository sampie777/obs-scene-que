package objects.que

import mocks.MockPlugin
import mocks.MockPlugin2
import kotlin.test.*

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

        assertEquals(3, Que.size())
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
}