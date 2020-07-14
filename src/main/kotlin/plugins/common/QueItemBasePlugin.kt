package plugins.common

import objects.que.JsonQueue
import objects.que.QueItem
import plugins.PluginLoader
import javax.swing.JComponent

interface QueItemBasePlugin : BasePlugin {

    /**
     * The name to display in the Source tabs
     */
    val tabName: String

    override fun enable() {
        super.enable()
        PluginLoader.registerQueItemPlugin(this)
    }

    override fun disable() {
        super.disable()
        PluginLoader.unregisterQueItemPlugin(this)
    }

    /**
     * Renders the panel component for the Sources panel (left panel of the main split pane)
     */
    fun sourcePanel(): JComponent

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem)", "objects.que.JsonQue")
    )
    fun configStringToQueItem(value: String): QueItem
    fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem
}