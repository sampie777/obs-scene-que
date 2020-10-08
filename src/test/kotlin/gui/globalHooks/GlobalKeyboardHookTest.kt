package gui.globalHooks


import config.Config
import mocks.MockPlugin
import objects.json.NativeKeyEventJson
import objects.que.Que
import org.jnativehook.keyboard.NativeKeyEvent
import kotlin.test.*

@Suppress("DEPRECATION")
class GlobalKeyboardHookTest {

    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        Config.globalKeyEventNextQueueItem = null
        Config.globalKeyEventPreviousQueueItem = null
        Que.clear()
    }

    @Test
    fun testCalibrationRunsCallbackOnce() {
        val keyEvent = NativeKeyEvent(0, 0, 0, 0, 'a')
        var callbackWasRun = false

        // When
        GlobalKeyboardHook.startCalibration {
            callbackWasRun = true
            assertEquals('a', it.keyChar)
        }

        // Then
        assertFalse(callbackWasRun)

        // When
        GlobalKeyboardHook.processKeyEvent(keyEvent)

        // Then
        assertTrue(callbackWasRun)

        // When
        callbackWasRun = false
        GlobalKeyboardHook.processKeyEvent(keyEvent)

        // Then
        assertFalse(callbackWasRun)
    }

    @Test
    fun testNextQueueIsActivatedForKeyEvent() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)
        val keyEvent = NativeKeyEvent(0, 0, 0, 0, 'a')
        Config.globalKeyEventNextQueueItem = NativeKeyEventJson.fromEvent(keyEvent)

        assertEquals(-1, Que.currentIndex())
        assertNull(Que.current())

        // When
        GlobalKeyboardHook.processKeyEvent(keyEvent)

        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())
    }

    @Test
    fun testPreviousQueueIsActivatedForKeyEvent() {
        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)
        Que.next()
        Que.next()
        val keyEvent = NativeKeyEvent(0, 0, 0, 0, 'a')
        Config.globalKeyEventPreviousQueueItem = NativeKeyEventJson.fromEvent(keyEvent)

        assertEquals(1, Que.currentIndex())
        assertEquals(item2, Que.current())

        // When
        GlobalKeyboardHook.processKeyEvent(keyEvent)

        assertEquals(0, Que.currentIndex())
        assertEquals(item1, Que.current())
    }

    @Test
    fun testLockModifiersGetStrippedFromKeyEvent() {
        val keyEvent = NativeKeyEvent(
            0,
            NativeKeyEvent.CTRL_MASK
                    + NativeKeyEvent.NUM_LOCK_MASK
                    + NativeKeyEvent.SCROLL_LOCK_MASK
                    + NativeKeyEvent.CAPS_LOCK_MASK,
            0,
            0,
            'a'
        )

        GlobalKeyboardHook.startCalibration {
            assertEquals(NativeKeyEvent.CTRL_MASK, it.modifiers)
        }
        GlobalKeyboardHook.processKeyEvent(keyEvent)
    }
}