import gui.Refreshable
import mocks.GuiComponentMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GUITest {

    @BeforeTest
    fun before() {
        GUI.unregisterAll()
    }

    @Test
    fun testRegisteringComponent() {
        val component = GuiComponentMock()
        GUI.register(component)

        assertEquals(1, GUI.registeredComponents().size)
        assertTrue(GUI.isRegistered(component))
    }

    @Test
    fun testRegisteringSameComponentTwice() {
        val component = GuiComponentMock()

        GUI.register(component)
        GUI.register(component)

        assertEquals(1, GUI.registeredComponents().size)
    }

    @Test
    fun testRegisteringSameClassButDifferentObject() {
        GUI.register(GuiComponentMock())
        GUI.register(GuiComponentMock())

        assertEquals(2, GUI.registeredComponents().size)
    }

    @Test
    fun testUnregisterAll() {
        GUI.register(GuiComponentMock())
        GUI.register(GuiComponentMock())

        GUI.unregisterAll()

        assertEquals(0, GUI.registeredComponents().size)
    }

    @Test
    fun testUnregisteringComponent() {
        val component = GuiComponentMock()
        GUI.register(component)
        assertEquals(1, GUI.registeredComponents().size)

        GUI.unregister(component)

        assertEquals(0, GUI.registeredComponents().size)
    }

    @Test
    fun testRemovingComponentWhileIteratingOverComponentsAndNotCrashing() {
        class GuiRemovingComponentMock : Refreshable {
            init {
                GUI.register(this)
            }

            override fun refreshNotifications() {
                GUI.unregister(this)
            }
        }

        GuiRemovingComponentMock()  // mock 1
        GuiRemovingComponentMock()  // mock 2
        GuiRemovingComponentMock()  // mock 3
        assertEquals(3, GUI.registeredComponents().size)

        GUI.refreshNotifications()

        assertEquals(0, GUI.registeredComponents().size)
    }
}