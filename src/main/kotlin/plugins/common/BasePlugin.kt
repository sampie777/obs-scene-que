package plugins.common

import objects.que.JsonQue
import javax.swing.Icon
import javax.swing.JComponent

interface BasePlugin {

    val name: String
    val description: String
    val version: String

    val icon: Icon?

    /**
     * The name to display in the Source tabs
     */
    val tabName: String

    fun enable() {}
    fun disable() {}

    /**
     * Renders the panel component for the Sources panel (left panel of the main split pane)
     */
    fun sourcePanel(): JComponent

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("jsonToQueItem(jsonQueItem: JsonQue.QueItem)", "objects.que.JsonQue")
    )
    fun configStringToQueItem(value: String): QueItem
    fun jsonToQueItem(jsonQueItem: JsonQue.QueItem): QueItem
}