package gui.forminputs

import config.Config
import gui.config.formcomponents.NumberFormInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NumberFormInputTest {

    @Test
    fun testNumberFormInputWithInt() {
        val input = NumberFormInput<Int>("mainPanelDividerLeftLocation", "label", 0, 100)
        Config.mainPanelDividerLeftLocation = 5
        input.component()

        assertEquals(5, input.value())
        val value: Any = input.value()
        assertTrue(value is Int)
        assertTrue(input.validate().isEmpty())

        Config.mainPanelDividerLeftLocation = 7
        input.save()
        assertEquals(5, Config.mainPanelDividerLeftLocation)
    }

    @Test
    fun testNumberFormInputWithLong() {
        val input = NumberFormInput<Long>("obsReconnectionTimeout", "label", 0, 10)
        Config.obsReconnectionTimeout = 5
        input.component()

        assertEquals(5, input.value())
        val value: Any = input.value()
        assertTrue(value is Long)
        assertTrue(input.validate().isEmpty())

        Config.obsReconnectionTimeout = 7
        input.save()
        assertEquals(5, Config.obsReconnectionTimeout)
    }
}