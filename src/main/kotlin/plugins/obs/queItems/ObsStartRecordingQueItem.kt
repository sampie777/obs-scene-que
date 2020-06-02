package plugins.obs.queItems

import objects.OBSClient
import objects.que.QueItem
import plugins.obs.ObsPlugin
import java.awt.Color

class ObsStartRecordingQueItem(override val plugin: ObsPlugin) : QueItem {
    override val name: String = "Start recording"
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    override fun toString() = name

    override fun activate() {
        OBSClient.getController()!!.startRecording {}
    }
}