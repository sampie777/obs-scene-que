package gui.menu

import mocks.MockPlugin
import mocks.MockPlugin2
import mocks.MockPlugin3
import objects.notifications.Notifications
import plugins.PluginLoader
import kotlin.test.*

class PluginMenuTest {

    @BeforeTest
    fun before() {
        PluginLoader.allPlugins.clear()
        Notifications.clear()
    }

    @Test
    fun testAddPluginNoMenuItems() {
        PluginLoader.allPlugins.add(MockPlugin())

        val menu = PluginMenu()

        assertEquals(0, menu.menuComponentCount)
        assertFalse(menu.isVisible)
        assertEquals(0, Notifications.unreadNotifications)
    }

    @Test
    fun testFailedToAddPluginMenuItems() {
        PluginLoader.allPlugins.add(MockPlugin2())

        val menu = PluginMenu()

        assertEquals(0, menu.menuComponentCount)
        assertFalse(menu.isVisible)
        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(Notifications.list[0].message.contains("MockPlugin2"))
    }

    @Test
    fun testAddTheSamePluginMenuItems() {
        PluginLoader.allPlugins.add(MockPlugin3())
        PluginLoader.allPlugins.add(MockPlugin3())

        val menu = PluginMenu()

        assertEquals(2, menu.menuComponentCount)
        assertTrue(menu.isVisible)
        assertEquals(0, Notifications.unreadNotifications)
    }
}