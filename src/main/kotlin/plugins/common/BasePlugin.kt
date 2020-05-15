package plugins.common

import javax.swing.JComponent

interface BasePlugin {

    val name: String
    val description: String

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

    fun configStringToQueItem(value: String): QueItem = EmptyQueItem(value)
}