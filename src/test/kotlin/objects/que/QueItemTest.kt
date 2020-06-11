package objects.que

import mocks.MockPlugin
import mocks.QueItemMock
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals


class QueItemTest {

    private val plugin = MockPlugin()

    @Test
    fun testClone() {
        val queItem1 = QueItemMock(plugin, "item1")

        val queItem2 = queItem1.clone()
        assertNotEquals(queItem1, queItem2)

        queItem1.executeAfterPrevious = true
        assertFalse(queItem2.executeAfterPrevious)
    }

}