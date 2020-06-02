package plugins.obs.queItems

import objects.OBSClient
import objects.que.QueItem
import plugins.obs.ObsPlugin
import java.awt.Color

class ObsStopRecordingQueItem(override val plugin: ObsPlugin) : QueItem {
    override val name: String = "Stop recording"
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    override fun toString() = name

    override fun activate() {
        OBSClient.getController()!!.stopRecording {}
    }
}