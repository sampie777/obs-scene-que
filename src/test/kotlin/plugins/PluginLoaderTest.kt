package plugins

import kotlin.test.Test
import kotlin.test.assertEquals

class PluginLoaderTest {

    @Test
    fun testLoadInternalPlugins() {
        PluginLoader.plugins.clear()
        PluginLoader.loadInternalPlugins()

        assertEquals(2, PluginLoader.plugins.size)
    }
}