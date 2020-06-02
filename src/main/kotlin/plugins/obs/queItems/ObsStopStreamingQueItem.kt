package plugins.obs.queItems

import objects.OBSClient
import objects.que.QueItem
import plugins.obs.ObsPlugin
import java.awt.Color

class ObsStopStreamingQueItem(override val plugin: ObsPlugin) : QueItem {
    override val name: String = "Stop streaming"
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    override fun toString() = name

    override fun activate() {
        OBSClient.getController()!!.stopStreaming {}
    }
}