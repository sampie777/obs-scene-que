package gui.forminputs

import config.Config
import gui.config.formcomponents.StringFormInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringFormInputTest {

    @Test
    fun testStringFormInput() {
        val input = StringFormInput("obsAddress", "label", allowEmpty = false)
        Config.obsAddress = "someaddress"
        input.component()

        assertEquals("someaddress", input.value())
        assertTrue(input.validate().isEmpty())

        Config.obsAddress = ""
        input.save()
        assertEquals("someaddress", input.value())
        assertEquals("someaddress", Config.obsAddress)
    }

    @Test
    fun testStringFormInputValidationWithWrongInput() {
        val input = StringFormInput("obsAddress", "label", allowEmpty = false)
        Config.obsAddress = ""
        input.component()

        assertEquals("", input.value())
        assertEquals(1, input.validate().size)

        Config.obsAddress = "someaddress"

        assertEquals("", input.value())
        assertFalse(input.validate().isEmpty())
    }

    @Test
    fun testStringFormInputAllowEmptyValidationWithWrongInput() {
        val input = StringFormInput("obsAddress", "label", allowEmpty = true)
        Config.obsAddress = ""
        input.component()

        assertEquals("", input.value())
        assertTrue(input.validate().isEmpty())

        Config.obsAddress = "someaddress"
        input.save()

        assertEquals("", input.value())
        assertEquals("", Config.obsAddress)
    }
}