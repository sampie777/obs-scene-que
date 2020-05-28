package objects.que

import config.Config
import mocks.MockPlugin
import objects.notifications.Notifications
import plugins.PluginLoader
import plugins.obs.ObsPlugin
import plugins.obs.ObsSceneQueItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("DEPRECATION")
class QueLoaderTest {

    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
        Notifications.clear()
        PluginLoader.queItemPlugins.clear()
    }

    @Test
    fun testStringToQueItem() {
        val obsPlugin = ObsPlugin()
        PluginLoader.registerQueItemPlugin(obsPlugin)
        val string = obsPlugin.name + "|false|Scene 1"
        val item = QueLoader.loadQueItemForStringLine(string)

        assertTrue(item is ObsSceneQueItem)
        assertEquals("Scene 1", item.name)
    }

    @Test
    fun testQueToString() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))

        val string = QueLoader.queToString()

        assertEquals("MockPlugin|false|1\nMockPlugin|false|2\nMockPlugin|false|3", string)
    }

    @Test
    fun testQueGetsClearedWhenNoQueFileIsFound() {
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))
        Que.add(mockPlugin.configStringToQueItem("3"))
        Config.queFile = "garbageFile"

        QueLoader.load()

        assertEquals(0, Que.size())
    }

    @Test
    fun testSaveQueToJson() {
        Que.name = "testQueue"
        Que.applicationVersion = "0.1.0"
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))

        val json = QueLoader.queToJson()

        assertEquals("""
            {
              "name": "testQueue",
              "applicationVersion": "0.1.0",
              "queItems": [
                {
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "1",
                  "executeAfterPrevious": false,
                  "data": {}
                },
                {
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "2",
                  "executeAfterPrevious": false,
                  "data": {}
                }
              ]
            }
        """.trimIndent(), json)
    }

    @Test
    fun testLoadQueFromJson() {
        PluginLoader.registerQueItemPlugin(mockPlugin)
        val json = """
            {
              "name": "testQueue",
              "applicationVersion": "0.1.0",
              "queItems": [
                {
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "1",
                  "executeAfterPrevious": false,
                  "data": {}
                },
                {
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "2",
                  "executeAfterPrevious": false,
                  "data": {}
                }
              ]
            }
        """

        QueLoader.fromJson(json)

        assertEquals("testQueue", Que.name)
        assertEquals("0.1.0", Que.applicationVersion)
        assertEquals("1", Que.getAt(0)?.name)
        assertEquals("2", Que.getAt(1)?.name)
    }

    @Test
    fun testLoadQueFromJsonGivesError() {
        PluginLoader.registerQueItemPlugin(mockPlugin)
        Que.name = "testQueue"
        Que.applicationVersion = "0.1.0"
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))

        val json = "invalid"
        QueLoader.fromJson(json)

        assertEquals(1, Notifications.unreadNotifications)
        assertEquals("testQueue", Que.name)
        assertEquals("0.1.0", Que.applicationVersion)
        assertEquals("1", Que.getAt(0)?.name)
        assertEquals("2", Que.getAt(1)?.name)
    }
}