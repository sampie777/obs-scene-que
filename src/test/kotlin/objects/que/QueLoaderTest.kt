package objects.que

import config.Config
import mocks.MockPlugin
import mocks.MockPlugin2
import mocks.QueItemMock
import mocks.QueItemMock2
import objects.notifications.Notifications
import plugins.PluginLoader
import plugins.obs.ObsPlugin
import plugins.obs.queItems.ObsSceneQueItem
import java.awt.Color
import kotlin.test.*

@Suppress("DEPRECATION")
class QueLoaderTest {

    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
        Notifications.clear()
        PluginLoader.queItemPlugins.clear()
        QueLoader.writeToFile = false
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
    fun testLoadQueFromValidFile() {
        PluginLoader.queItemPlugins.add(ObsPlugin())
        Config.queFile = javaClass.getResource("/objects/que/que1_valid.json").file

        QueLoader.load()

        assertEquals(0, Notifications.unreadNotifications)
        assertEquals("que1_valid", Que.name)
        assertEquals(5, Que.size())
    }

    @Test
    fun testLoadQueFromInvalidItemsFile() {
        PluginLoader.queItemPlugins.add(ObsPlugin())
        Config.queFile = javaClass.getResource("/objects/que/que2_invalidItems.json").file
        Que.name = "default-que"
        Que.add(QueItemMock(MockPlugin(), "item1"))

        QueLoader.load()

        assertEquals(1, Notifications.list.size)
        assertTrue(Notifications.list[0].message.contains("Failed to load Queue from json"))
        assertEquals("default-que", Que.name)
        assertEquals(1, Que.size())
        assertEquals("item1", Que.getList()[0].name)
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
    fun testLoadQueFromDirectoryFileGivesError() {
        Config.queFile = javaClass.getResource("/objects/que").file
        Que.name = "default-que"
        Que.add(QueItemMock(MockPlugin(), "item1"))

        QueLoader.load()

        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(Notifications.list[0].message.contains("Failed to read file"))
        assertEquals("default-que", Que.name)
        assertEquals(1, Que.size())
        assertEquals("item1", Que.getList()[0].name)
    }

    @Test
    fun testSaveQueToJson() {
        Que.name = "testQueue"
        Que.applicationVersion = "0.1.0"
        val item1 = mockPlugin.configStringToQueItem("1")
        item1.quickAccessColor = Color(0, 100, 200)
        Que.add(item1)
        Que.add(mockPlugin.configStringToQueItem("2"))

        val json = QueLoader.queToJsonString()

        assertEquals(
            """
{
  "name": "testQueue",
  "applicationVersion": "0.1.0",
  "queueItems": [
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "1",
      "executeAfterPrevious": false,
      "quickAccessColor": {
        "value": -16751416,
        "frgbvalue": null,
        "fvalue": null,
        "falpha": 0.0,
        "cs": null
      },
      "data": {}
    },
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "2",
      "executeAfterPrevious": false,
      "quickAccessColor": null,
      "data": {}
    }
  ],
  "apiVersion": 1
}
        """.trimIndent(), json
        )
    }

    @Test
    fun testQueToJsonWithInvalidItems() {
        Que.name = "testQueue"
        Que.applicationVersion = "0.1.0"
        Que.add(QueItemMock2(mockPlugin, "1"))
        Que.add(QueItemMock(mockPlugin, "2"))
        Que.add(QueItemMock2(mockPlugin, "3"))

        val json = QueLoader.queToJsonString()

        assertEquals(
            """
            {
              "name": "testQueue",
              "applicationVersion": "0.1.0",
              "queueItems": [
                {
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "2",
                  "executeAfterPrevious": false,
                  "quickAccessColor": null,
                  "data": {}
                }
              ],
              "apiVersion": 1
            }
        """.trimIndent(), json
        )

        assertEquals(2, Notifications.unreadNotifications)
        assertEquals("Failed to save queue item 1: Oops", Notifications.list[0].message)
        assertEquals("Failed to save queue item 3: Oops", Notifications.list[1].message)
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
                  "quickAccessColor": {
                    "value": -16751416,
                    "falpha": 0.0
                  },
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

        val result = QueLoader.fromJson(json)

        assertEquals("testQueue", Que.name)
        assertEquals("0.1.0", Que.applicationVersion)
        assertEquals("1", Que.getAt(0)?.name)
        assertEquals(Color(0, 100, 200), Que.getAt(0)?.quickAccessColor)
        assertEquals("2", Que.getAt(1)?.name)
        assertNull(Que.getAt(2)?.quickAccessColor)
        assertTrue(result)
    }

    @Test
    fun testLoadQueFromJsonGivesError() {
        PluginLoader.registerQueItemPlugin(mockPlugin)
        Que.name = "testQueue"
        Que.applicationVersion = "0.1.0"
        Que.add(mockPlugin.configStringToQueItem("1"))
        Que.add(mockPlugin.configStringToQueItem("2"))

        val json = "invalid"
        val result = QueLoader.fromJson(json)

        assertEquals(1, Notifications.unreadNotifications)
        assertEquals("testQueue", Que.name)
        assertEquals("0.1.0", Que.applicationVersion)
        assertEquals("1", Que.getAt(0)?.name)
        assertEquals("2", Que.getAt(1)?.name)
        assertFalse(result)
    }

    @Test
    fun testJsonQueItemFromJson() {
        val json = """{
                  "pluginName": "MockPlugin",
                  "className": "QueItemMock",
                  "name": "1",
                  "executeAfterPrevious": false,
                  "quickAccessColor": {
                    "value": -16751416,
                    "falpha": 0.0
                  },
                  "data": {}
                }"""

        val item = QueLoader.jsonQueItemFromJson(json)

        assertNotNull(item)
        assertEquals("MockPlugin", item.pluginName)
        assertEquals("QueItemMock", item.className)
        assertEquals("1", item.name)
        assertFalse(item.executeAfterPrevious)
        assertEquals(Color(0, 100, 200), item.quickAccessColor)
    }

    @Test
    fun testJsonQueItemFromInvalidJson() {
        val json = """{
                  "pluginNa
                  "data": {}
                }"""

        val item = QueLoader.jsonQueItemFromJson(json)

        assertNull(item)
        assertEquals(1, Notifications.unreadNotifications)
        assertEquals(
            "Failed to load Queue Item from json: com.google.gson.stream.MalformedJsonException: Expected ':' at line 3 column 21 path \$.pluginNa\n" +
                    "                  ",
            Notifications.list[0].message
        )
    }

    @Test
    fun testLoadQueItemFromJsonQueItem() {
        PluginLoader.queItemPlugins.add(mockPlugin)
        val jsonQueItem = JsonQueue.QueueItem(
            mockPlugin.name, "QueItemMock",
            "name", false,
            null, hashMapOf()
        )

        val queItem = QueLoader.loadQueItemFromJson(jsonQueItem)

        assertEquals(0, Notifications.unreadNotifications)
        assertNotNull(queItem)
        assertEquals("name", queItem.name)
        assertFalse(queItem.executeAfterPrevious)
        assertNull(queItem.quickAccessColor)
    }

    @Test
    fun testLoadQueItemFromJsonQueItemWithoutPlugins() {
        val jsonQueItem = JsonQueue.QueueItem(
            mockPlugin.name, "QueItemMock",
            "", false,
            null, hashMapOf()
        )

        val result = QueLoader.loadQueItemFromJson(jsonQueItem)

        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(Notifications.list[0].message.contains("not found"))
        assertNull(result)
    }

    @Test
    fun testLoadQueItemFromJsonQueItemWithInvalidQueItemMethod() {
        val plugin = MockPlugin2()
        PluginLoader.queItemPlugins.add(plugin)
        val jsonQueItem = JsonQueue.QueueItem(
            plugin.name, "QueItemMock2",
            "name", false,
            null, hashMapOf()
        )

        val result = QueLoader.loadQueItemFromJson(jsonQueItem)

        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(Notifications.list[0].message.contains("Failed to load queue item"))
        assertNull(result)
    }

    @Test
    fun testSaveQue() {
        Que.name = "name1"
        Config.queFile = javaClass.getResource("/objects/que/que1_valid.json").file

        QueLoader.save()

        assertEquals(0, Notifications.unreadNotifications)
        assertEquals("que1_valid", Que.name)
    }
}