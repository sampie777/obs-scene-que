package objects

import kotlin.test.*

class QueTest {

    @BeforeTest
    fun before() {
        Que.clear()
    }

    @Test
    fun testAddAtSpecificPoint() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        val scene = TScene()
        scene.name = "insert"

        Que.add(1, scene)

        assertEquals(4, Que.size())
        assertEquals(scene, Que.getAt(1))
    }

    @Test
    fun testAddDoubleItems() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        val scene = TScene()
        scene.name = scene2.name

        Que.add(2, scene)

        assertEquals(3, Que.size())
    }

    @Test
    fun testNext() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        assertEquals(-1, Que.currentIndex())
        assertNull(Que.current())

        Que.next()
        assertEquals(0, Que.currentIndex())
        assertEquals(scene1, Que.current())

        Que.next()
        assertEquals(1, Que.currentIndex())
        assertEquals(scene2, Que.current())

        Que.next()
        assertEquals(2, Que.currentIndex())
        assertEquals(scene3, Que.current())

        Que.next()
        assertEquals(2, Que.currentIndex())
        assertEquals(scene3, Que.current())
    }

    @Test
    fun testPrevious() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        Que.setCurrentSceneByIndex(2)
        assertEquals(2, Que.currentIndex())
        assertEquals(scene3, Que.current())

        Que.previous()
        assertEquals(1, Que.currentIndex())
        assertEquals(scene2, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(scene1, Que.current())

        Que.previous()
        assertEquals(0, Que.currentIndex())
        assertEquals(scene1, Que.current())
    }

    @Test
    fun testGetAt() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        assertNull(Que.getAt( -1))
        assertNull(Que.getAt( 5))
        assertEquals(scene2, Que.getAt(1))
    }

    @Test
    fun testSetCurrentSceneByIndex() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        Que.setCurrentSceneByIndex(1)
        assertEquals(1, Que.currentIndex())
        assertEquals(scene2, Que.current())
    }

    @Test
    fun testSetCurrentSceneByName() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        Que.setCurrentSceneByName("2")
        assertEquals(1, Que.currentIndex())
        assertEquals(scene2, Que.current())
    }

    @Test
    fun testMove() {
        val scene1 = TScene()
        scene1.name = "1"
        val scene2 = TScene()
        scene2.name = "2"
        val scene3 = TScene()
        scene3.name = "3"
        Que.add(scene1)
        Que.add(scene2)
        Que.add(scene3)

        assertTrue(Que.move(0, 1))  // Nothing happens
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertTrue(Que.move(0, 2))
        assertEquals(scene2, Que.getAt(0))
        assertEquals(scene1, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertTrue(Que.move(1, 0))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertTrue(Que.move(0, 3))
        assertEquals(scene2, Que.getAt(0))
        assertEquals(scene3, Que.getAt(1))
        assertEquals(scene1, Que.getAt(2))

        assertTrue(Que.move(2, 0))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertFalse(Que.move(0, 4))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertFalse(Que.move(-1, 2))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertFalse(Que.move(4, 0))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))

        assertFalse(Que.move(0, -1))
        assertEquals(scene1, Que.getAt(0))
        assertEquals(scene2, Que.getAt(1))
        assertEquals(scene3, Que.getAt(2))
    }
}