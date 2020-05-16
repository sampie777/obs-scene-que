package plugins

import kotlin.test.Test
import kotlin.test.assertEquals

class PluginLoaderTest {

    @Test
    fun testLoadInternalPlugins() {
        PluginLoader.loadInternalPlugins()

        assertEquals(3, PluginLoader.plugins.size)
    }
}