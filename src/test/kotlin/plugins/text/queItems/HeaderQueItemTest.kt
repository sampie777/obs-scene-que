package plugins.text.queItems

import objects.que.Que
import plugins.text.TextPlugin
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HeaderQueItemTest {

    private val plugin = TextPlugin()

    @BeforeTest
    fun before() {
        Que.clear()
    }

    @Test
    fun testActivateWillGoToNextQueItem() {
        Que.add(PlainTextQueItem(plugin, "1"))
        Que.add(HeaderQueItem(plugin, "2"))
        Que.add(PlainTextQueItem(plugin, "3"))
        Que.add(PlainTextQueItem(plugin, "4"))

        Que.next()
        assertEquals("1", Que.current()!!.name)

        Que.next()
        assertEquals("3", Que.current()!!.name)
    }

    @Test
    fun testActivatePreviousWillGoToPreviousQueItem() {
        Que.add(PlainTextQueItem(plugin, "1"))
        Que.add(HeaderQueItem(plugin, "2"))
        Que.add(PlainTextQueItem(plugin, "3"))
        Que.add(PlainTextQueItem(plugin, "4"))
        Que.setCurrentQueItemByName(plugin.name, "4")

        Que.previous()
        assertEquals("3", Que.current()!!.name)

        Que.previous()
        assertEquals("1", Que.current()!!.name)
    }
}