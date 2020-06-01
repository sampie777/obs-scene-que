package objects.que

import java.awt.Color

class JsonQue {
    data class Que(
        val name: String,
        val applicationVersion: String,
        val queItems: List<QueItem>
    )

    data class QueItem(
        val pluginName: String,
        val className: String,
        val name: String,
        val executeAfterPrevious: Boolean,
        val quickAccessColor: Color?,
        val data: HashMap<String, String>
    ) {
        override fun toString(): String = "JsonQue.QueItem[pluginName=$pluginName; className=$className; name=$name]"
    }
}