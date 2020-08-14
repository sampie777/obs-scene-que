package plugins.http

import gui.utils.createImageIcon
import objects.que.JsonQueue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import plugins.http.gui.SourcePanel
import plugins.http.queItems.HttpQueItem
import java.awt.Color
import javax.swing.Icon
import javax.swing.JComponent

@Suppress("unused")
class HttpPlugin : QueItemBasePlugin {
    override val name = "HttpPlugin"
    override val description = "Queue items for performing HTTP requests"
    override val version: String = "0.0.0"

    override val icon: Icon? = createImageIcon("/plugins/http/icon-14.png")

    override val tabName = "HTTP"

    internal val quickAccessColor = Color(229, 255, 254)

    override fun sourcePanel(): JComponent {
        return SourcePanel(this)
    }

    override fun configStringToQueItem(value: String): QueItem {
        TODO("Not yet implemented")
    }

    override fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem {
        return when (jsonQueueItem.className) {
            HttpQueItem::class.java.simpleName -> HttpQueItem.fromJson(this, jsonQueueItem)
            else -> throw IllegalArgumentException("Invalid HttpPlugin queue item: ${jsonQueueItem.className}")
        }
    }
}