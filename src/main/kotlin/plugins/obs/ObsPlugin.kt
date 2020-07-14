package plugins.obs

import gui.utils.createImageIcon
import objects.TScene
import objects.que.JsonQueue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import plugins.obs.gui.SourcePanel
import plugins.obs.queItems.*
import java.awt.Color
import javax.swing.Icon
import javax.swing.JComponent

@Suppress("unused")
class ObsPlugin : QueItemBasePlugin {
    override val name = "ObsPlugin"
    override val description = "Queue items for integration with OBS"
    override val version: String = "0.0.0"

    override val icon: Icon? = createImageIcon("/plugins/obs/icon-14.png")

    override val tabName = "OBS"

    internal val quickAccessColor = Color(229, 238, 255)

    override fun sourcePanel(): JComponent {
        return SourcePanel(this)
    }

    override fun configStringToQueItem(value: String): QueItem {
        return ObsSceneQueItem(this, TScene(value))
    }

    override fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem {
        return when (jsonQueueItem.className) {
            ObsSceneQueItem::class.java.simpleName -> ObsSceneQueItem(this, TScene(jsonQueueItem.name))
            ObsStartStreamingQueItem::class.java.simpleName -> ObsStartStreamingQueItem(this)
            ObsStopStreamingQueItem::class.java.simpleName -> ObsStopStreamingQueItem(this)
            ObsStartRecordingQueItem::class.java.simpleName -> ObsStartRecordingQueItem(this)
            ObsStopRecordingQueItem::class.java.simpleName -> ObsStopRecordingQueItem(this)
            ObsToggleSourceVisibilityQueItem::class.java.simpleName -> ObsToggleSourceVisibilityQueItem.fromJson(this, jsonQueueItem)
            else -> throw IllegalArgumentException("Invalid OBS Plugin queue item: ${jsonQueueItem.className}")
        }
    }
}