package config

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PropertyLoaderTest {

    @BeforeTest
    fun before() {
        ConfigMock.stringProperty1 = "stringValue1"
        ConfigMock.stringProperty2 = "stringValue2"
        ConfigMock.longProperty1 = 100
    }

    @Test
    fun testSavePropertiesForConfig() {
        PropertyLoader.saveConfig(ConfigMock::class.java)

        assertEquals("stringValue1", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("100", PropertyLoader.getUserProperties().getProperty("longProperty1"))
    }

    @Test
    fun testLoadPropertiesForConfig() {
        PropertyLoader.getUserProperties().setProperty("stringProperty1", "stringValue1.1")
        PropertyLoader.getUserProperties().setProperty("stringProperty2", "stringValue2.1")
        PropertyLoader.getUserProperties().setProperty("longProperty1", "200")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals("stringValue1.1", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2.1", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("200", PropertyLoader.getUserProperties().getProperty("longProperty1"))

        assertEquals("stringValue1.1", ConfigMock.stringProperty1)
        assertEquals("stringValue2.1", ConfigMock.stringProperty2)
        assertEquals(200, ConfigMock.longProperty1)
    }

    @Test
    fun testLoadPropertiesForConfigWithHashmapNoItems() {
        PropertyLoader.getUserProperties().setProperty("hashMapProperty1", "")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals(0, ConfigMock.hashMapProperty1.size)
    }

    @Test
    fun testLoadPropertiesForConfigWithHashmapOneItem() {
        PropertyLoader.getUserProperties().setProperty("hashMapProperty1", "Scene 1%=>10")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals(1, ConfigMock.hashMapProperty1.size)
        assertTrue(ConfigMock.hashMapProperty1.containsKey("Scene 1"))
        assertEquals(10, ConfigMock.hashMapProperty1["Scene 1"])
    }

    @Test
    fun testLoadPropertiesForConfigWithHashmapMultipleItems() {
        PropertyLoader.getUserProperties().setProperty("hashMapProperty1", "Scene 1%=>10%;;Scene 2%=>20")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals(2, ConfigMock.hashMapProperty1.size)
        assertTrue(ConfigMock.hashMapProperty1.containsKey("Scene 1"))
        assertTrue(ConfigMock.hashMapProperty1.containsKey("Scene 2"))
        assertEquals(10, ConfigMock.hashMapProperty1["Scene 1"])
        assertEquals(20, ConfigMock.hashMapProperty1["Scene 2"])
    }
}