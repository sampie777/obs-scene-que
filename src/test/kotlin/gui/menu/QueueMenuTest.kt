package gui.menu

import config.Config
import objects.notifications.Notifications
import objects.que.Que
import objects.que.QueLoader
import plugins.PluginLoader
import plugins.obs.ObsPlugin
import javax.swing.JMenuItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class QueueMenuTest {

    @BeforeTest
    fun before() {
        Que.clear()
        Notifications.clear()
        PluginLoader.queItemPlugins.clear()
        QueLoader.writeToFile = false
        Config.recentQueueFiles.clear()
        Config.queFile = ""
    }

    @Test
    fun testOnlyValidRecentFilesAppearInRecentFilesMenu() {
        val validFilePath = javaClass.getResource("/objects/que/que1_valid.json").file
        val invalidFilePath = validFilePath + "gibberish"

        val menu = QueueMenu()
        assertEquals(0, menu.recentFilesMenu.menuComponentCount)
        assertEquals(0, Config.recentQueueFiles.size)

        Config.recentQueueFiles.add(validFilePath)
        menu.initRecentFilesMenu()
        assertEquals(1, menu.recentFilesMenu.menuComponentCount)
        assertEquals(1, Config.recentQueueFiles.size)

        Config.recentQueueFiles.clear()
        menu.initRecentFilesMenu()
        assertEquals(0, menu.recentFilesMenu.menuComponentCount)
        assertEquals(0, Config.recentQueueFiles.size)

        Config.recentQueueFiles.add(invalidFilePath)
        menu.initRecentFilesMenu()
        assertEquals(0, menu.recentFilesMenu.menuComponentCount)
        assertEquals(0, Config.recentQueueFiles.size)

        Config.recentQueueFiles.add(validFilePath)
        Config.recentQueueFiles.add(invalidFilePath)
        menu.initRecentFilesMenu()
        assertEquals(1, menu.recentFilesMenu.menuComponentCount)
        assertEquals(1, Config.recentQueueFiles.size)
        assertEquals(validFilePath, Config.recentQueueFiles.first())
    }

    @Test
    fun testOpenRecentFileLoadsQueue() {
        val validFilePath = javaClass.getResource("/objects/que/que1_valid.json").file
        Config.recentQueueFiles.add(validFilePath)
        PluginLoader.queItemPlugins.add(ObsPlugin())
        val menu = QueueMenu()

        (menu.recentFilesMenu.menuComponents.first() as JMenuItem).doClick()

        assertEquals(validFilePath, Config.queFile)
        assertEquals(0, Notifications.unreadNotifications)
        assertEquals("que1_valid", Que.name)
        assertEquals(5, Que.size())
    }

}