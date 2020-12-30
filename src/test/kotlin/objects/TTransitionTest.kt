package objects

import kotlin.test.Test
import kotlin.test.assertEquals

class TTransitionTest {

    @Test
    fun testTransitionNameGivesName() {
        val transition = TTransition("transition")
        assertEquals("transition", transition.name)
    }

    @Test
    fun testTransitionToStringGivesName() {
        val transition = TTransition("transition")
        assertEquals("transition", transition.toString())
    }

    @Test
    fun testEmptyTransitionGivesEmptyString() {
        val transition = TTransition(null)
        assertEquals("", transition.toString())
    }
}