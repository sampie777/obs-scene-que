package config

import objects.notifications.Notifications
import kotlin.test.*

class ConfigTest {

    @BeforeTest
    fun before() {
        Notifications.list.clear()
    }

    @Test
    fun testConfigGetExistingKey() {
        Config.obsAddress = "x"

        assertEquals("x", Config.get("obsAddress"))
        assertTrue(Notifications.list.isEmpty())
    }

    @Test
    fun testConfigGetNonExistingKey() {
        assertNull(Config.get("nonexistingkey"))
        assertEquals(1, Notifications.list.size)
        assertEquals("Could not get configuration setting: nonexistingkey", Notifications.list[0].message)
    }

    @Test
    fun testConfigSetExistingKey() {
        Config.obsAddress = "x"

        Config.set("obsAddress", "new")
        assertEquals("new", Config.obsAddress)
        assertTrue(Notifications.list.isEmpty())
    }

    @Test
    fun testConfigSetNonExistingKey() {
        Config.set("nonexistingkey", "x")

        assertEquals(1, Notifications.list.size)
        assertEquals("Could not set configuration setting: nonexistingkey", Notifications.list[0].message)
        assertNull(Config.get("nonexistingkey"))
    }
}