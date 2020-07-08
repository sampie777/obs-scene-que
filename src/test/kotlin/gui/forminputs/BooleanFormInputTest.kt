package gui.forminputs

import config.Config
import gui.config.formcomponents.BooleanFormInput
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BooleanFormInputTest {

    @AfterTest
    fun after() {
        Config.enableApplicationLoggingToFile = false
    }

    @Test
    fun testBooleanFormInputWithTrueValue() {
        val input = BooleanFormInput("enableApplicationLoggingToFile", "label")
        Config.enableApplicationLoggingToFile = true
        input.component()

        assertEquals(true, input.value())
        assertTrue(input.validate().isEmpty())

        Config.enableApplicationLoggingToFile = false
        input.save()
        assertEquals(true, input.value())
        assertEquals(true, Config.enableApplicationLoggingToFile)
    }

    @Test
    fun testBooleanFormInputWithFalseValue() {
        val input = BooleanFormInput("enableApplicationLoggingToFile", "label")
        Config.enableApplicationLoggingToFile = false
        input.component()

        assertEquals(false, input.value())
        assertTrue(input.validate().isEmpty())

        Config.enableApplicationLoggingToFile = true
        input.save()
        assertEquals(false, input.value())
        assertEquals(false, Config.enableApplicationLoggingToFile)
    }
}