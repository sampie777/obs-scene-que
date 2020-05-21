package objects.que

import config.Config
import mocks.MockPlugin
import plugins.PluginLoader
import plugins.obs.ObsPlugin
import plugins.obs.ObsSceneQueItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QueLoaderTest {

    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
    }

    @Test
    fun testStringToQueItem() {
        val obsPlugin = ObsPlugin()
        PluginLoader.plugins.add(obsPlugin)
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
}