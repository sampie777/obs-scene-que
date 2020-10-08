package objects.json

import org.jnativehook.keyboard.NativeKeyEvent
import kotlin.test.*


class NativeKeyEventJsonTest {

    @Test
    fun testJsonToEvent() {
        val json = NativeKeyEventJson(1, 2, 3, 'a')
        val keyEvent = json.toEvent()

        assertEquals(NativeKeyEvent.NATIVE_KEY_RELEASED, keyEvent.id)
        assertEquals(1, keyEvent.modifiers)
        assertEquals(2, keyEvent.rawCode)
        assertEquals(3, keyEvent.keyCode)
        assertEquals('a', keyEvent.keyChar)
    }

    @Test
    fun testEventToJson() {
        val keyEvent = NativeKeyEvent(0, 1, 2, 3, 'a')
        val json = NativeKeyEventJson.fromEvent(keyEvent)

        assertEquals(1, json.modifiers)
        assertEquals(2, json.rawCode)
        assertEquals(3, json.keyCode)
        assertEquals('a', json.keyChar)
    }

    @Test
    fun testJsonToString() {
        val json = NativeKeyEventJson(1, 2, 3, 'a')
        val string = json.toJson()

        assertEquals("""{"modifiers":1,"rawCode":2,"keyCode":3,"keyChar":"a"}""", string)
    }

    @Test
    fun testStringToJson() {
        val string = """{"modifiers":1,"rawCode":2,"keyCode":3,"keyChar":"a"}"""
        val json = NativeKeyEventJson.fromJson(string)

        assertEquals(1, json?.modifiers)
        assertEquals(2, json?.rawCode)
        assertEquals(3, json?.keyCode)
        assertEquals('a', json?.keyChar)
    }

    @Test
    fun testInvalidStringToJsonGivesNull() {
        val string = """]"""
        val json = NativeKeyEventJson.fromJson(string)

        assertNull(json)
    }

    @Test
    fun testIsEqualTo() {
        val json1 = NativeKeyEventJson(1, 2, 3, 'a')
        val json2 = NativeKeyEventJson(2, 2, 3, 'a')
        val keyEvent1 = NativeKeyEvent(0, 1, 2, 3, 'a')
        val keyEvent2 = NativeKeyEvent(0, 2, 2, 3, 'a')

        assertTrue(json1.isEqualTo(keyEvent1))
        assertFalse(json1.isEqualTo(keyEvent2))
        assertFalse(json2.isEqualTo(keyEvent1))
        assertTrue(json2.isEqualTo(keyEvent2))
    }
}