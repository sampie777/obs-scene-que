package plugins

import kotlin.test.Test
import kotlin.test.assertEquals

class PluginLoaderTest {

    @Test
    fun testLoadInternalPlugins() {
        PluginLoader.allPlugins.clear()
        PluginLoader.queItemPlugins.clear()

        PluginLoader.loadInternalPlugins()

        assertEquals(PluginLoader.internalPluginClasses.size, PluginLoader.allPlugins.size)
        assertEquals(0, PluginLoader.queItemPlugins.size)
    }

    @Test
    fun testEnableInternalPlugins() {
        PluginLoader.allPlugins.clear()
        PluginLoader.queItemPlugins.clear()
        PluginLoader.loadInternalPlugins()

        PluginLoader.enableAll()

        assertEquals(PluginLoader.internalPluginClasses.size, PluginLoader.allPlugins.size)
        assertEquals(PluginLoader.internalPluginClasses.size, PluginLoader.queItemPlugins.size)
    }
}