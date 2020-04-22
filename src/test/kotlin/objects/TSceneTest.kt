package objects

import kotlin.test.Test
import kotlin.test.assertEquals

class TSceneTest {

    @Test
    fun testSceneToStringGivesName() {
        val scene = TScene("scene 1")
        assertEquals("scene 1", scene.toString())
    }

    @Test
    fun testNullNamedSceneToStringGivesEmptyString() {
        val scene = TScene(null)
        assertEquals("", scene.toString())
    }
}