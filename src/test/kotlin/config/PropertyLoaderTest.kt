package config

import mocks.ConfigMock
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import kotlin.test.*

class PropertyLoaderTest {

    @BeforeTest
    fun before() {
        ConfigMock.stringProperty1 = "stringValue1"
        ConfigMock.stringProperty2 = "stringValue2"
        ConfigMock.longProperty1 = 100
        ConfigMock.nullableColorProperty1 = null
        ConfigMock.pointProperty1 = Point(5, 6)
        ConfigMock.dimensionProperty1 = Dimension(10, 11)
    }

    @Test
    fun testSavePropertiesForConfig() {
        PropertyLoader.saveConfig(ConfigMock::class.java)

        assertEquals("stringValue1", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("100", PropertyLoader.getUserProperties().getProperty("longProperty1"))
        assertEquals("5,6", PropertyLoader.getUserProperties().getProperty("pointProperty1"))
        assertEquals("10,11", PropertyLoader.getUserProperties().getProperty("dimensionProperty1"))
    }

    @Test
    fun testLoadPropertiesForConfig() {
        PropertyLoader.getUserProperties().setProperty("stringProperty1", "stringValue1.1")
        PropertyLoader.getUserProperties().setProperty("stringProperty2", "stringValue2.1")
        PropertyLoader.getUserProperties().setProperty("longProperty1", "200")
        PropertyLoader.getUserProperties().setProperty("pointProperty1", "15,16")
        PropertyLoader.getUserProperties().setProperty("dimensionProperty1", "20,21")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals("stringValue1.1", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2.1", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("200", PropertyLoader.getUserProperties().getProperty("longProperty1"))
        assertEquals("15,16", PropertyLoader.getUserProperties().getProperty("pointProperty1"))
        assertEquals("20,21", PropertyLoader.getUserProperties().getProperty("dimensionProperty1"))

        assertEquals("stringValue1.1", ConfigMock.stringProperty1)
        assertEquals("stringValue2.1", ConfigMock.stringProperty2)
        assertEquals(200, ConfigMock.longProperty1)
        assertEquals(Point(15, 16), ConfigMock.pointProperty1)
        assertEquals(Dimension(20, 21), ConfigMock.dimensionProperty1)
    }

    @Test
    fun testLoadPropertiesForConfigWithAbsentValues() {
        PropertyLoader.getUserProperties().remove("stringProperty1")
        PropertyLoader.getUserProperties().remove("stringProperty2")
        PropertyLoader.getUserProperties().remove("longProperty1")
        PropertyLoader.getUserProperties().remove("pointProperty1")
        PropertyLoader.getUserProperties().remove("dimensionProperty1")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals(null, PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals(null, PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals(null, PropertyLoader.getUserProperties().getProperty("longProperty1"))
        assertEquals(null, PropertyLoader.getUserProperties().getProperty("pointProperty1"))
        assertEquals(null, PropertyLoader.getUserProperties().getProperty("dimensionProperty1"))

        assertEquals("stringValue1", ConfigMock.stringProperty1)
        assertEquals("stringValue2", ConfigMock.stringProperty2)
        assertEquals(100, ConfigMock.longProperty1)
        assertEquals(Point(5, 6), ConfigMock.pointProperty1)
        assertEquals(Dimension(10, 11), ConfigMock.dimensionProperty1)
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

    @Test
    fun testLoadPropertiesForNullColorValue() {
        PropertyLoader.getUserProperties().setProperty("nullableColorProperty1", "")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertNull(ConfigMock.nullableColorProperty1)
    }

    @Test
    fun testLoadPropertiesForNotSpecifiedColorValue() {
        PropertyLoader.getUserProperties().remove("nullableColorProperty1")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertNull(ConfigMock.nullableColorProperty1)
    }

    @Test
    fun testLoadPropertiesForSpecifiedColorValue() {
        PropertyLoader.getUserProperties().setProperty("nullableColorProperty1", "255,100,0")

        PropertyLoader.loadConfig(ConfigMock::class.java)

        assertEquals(255, ConfigMock.nullableColorProperty1!!.red)
        assertEquals(100, ConfigMock.nullableColorProperty1!!.green)
        assertEquals(0, ConfigMock.nullableColorProperty1!!.blue)
    }

    @Test
    fun testSavePropertiesWithColorValue() {
        ConfigMock.nullableColorProperty1 = Color(255, 100, 0)

        PropertyLoader.saveConfig(ConfigMock::class.java)

        assertEquals("255,100,0", PropertyLoader.getUserProperties().getProperty("nullableColorProperty1"))
    }

    @Test
    fun testSavePropertiesWithNullValue() {
        ConfigMock.nullableColorProperty1 = null

        PropertyLoader.saveConfig(ConfigMock::class.java)

        assertEquals("", PropertyLoader.getUserProperties().getProperty("nullableColorProperty1"))
    }

    @Test
    fun testSaveConfigReturnsTrueForNewChanges() {
        PropertyLoader.saveConfig(ConfigMock::class.java)

        ConfigMock.stringProperty1 = "newValue"

        assertTrue(PropertyLoader.saveConfig(ConfigMock::class.java))
        assertEquals("newValue", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("100", PropertyLoader.getUserProperties().getProperty("longProperty1"))
    }

    @Test
    fun testSaveConfigReturnsFalseForNoChanges() {
        PropertyLoader.saveConfig(ConfigMock::class.java)

        assertFalse(PropertyLoader.saveConfig(ConfigMock::class.java))
        assertEquals("stringValue1", PropertyLoader.getUserProperties().getProperty("stringProperty1"))
        assertEquals("stringValue2", PropertyLoader.getUserProperties().getProperty("stringProperty2"))
        assertEquals("100", PropertyLoader.getUserProperties().getProperty("longProperty1"))
    }
}