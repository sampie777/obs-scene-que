package plugins.obs

import handles.QueItemTransferHandler
import plugins.obs.queItems.ObsStartRecordingQueItem
import plugins.obs.queItems.ObsStartStreamingQueItem
import plugins.obs.queItems.ObsStopRecordingQueItem
import plugins.obs.queItems.ObsStopStreamingQueItem
import java.awt.datatransfer.Transferable
import javax.swing.JComponent
import javax.swing.JList

class ObsActionQueItemTransferHandler(private val plugin: ObsPlugin) : QueItemTransferHandler() {

    override fun createTransferable(component: JComponent): Transferable {
        val list = component as JList<*>

        queItem = when (list.selectedValue) {
            is ObsStartRecordingQueItem -> ObsStartRecordingQueItem(plugin)
            is ObsStartStreamingQueItem -> ObsStartStreamingQueItem(plugin)
            is ObsStopRecordingQueItem -> ObsStopRecordingQueItem(plugin)
            is ObsStopStreamingQueItem -> ObsStopStreamingQueItem(plugin)
            else -> null
        }

        return super.createTransferable(component)
    }
}