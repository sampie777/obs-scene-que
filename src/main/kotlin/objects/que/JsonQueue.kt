package objects.que

import java.awt.Color

class JsonQueue {
    data class Queue(
        val name: String,
        val applicationVersion: String,
        val queueItems: List<QueueItem>,
        val apiVersion: Int = 1
    )

    data class QueueItem(
        val pluginName: String,
        val className: String,
        val name: String,
        val executeAfterPrevious: Boolean,
        val quickAccessColor: Color?,
        val data: HashMap<String, String>
    ) {
        override fun toString(): String = "JsonQueue.QueueItem[pluginName=$pluginName; className=$className; name=$name]"
    }
}