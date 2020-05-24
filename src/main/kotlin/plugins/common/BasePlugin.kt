package plugins.common

import javax.swing.Icon
import javax.swing.JMenu

interface BasePlugin {

    val name: String
    val description: String
    val version: String

    val icon: Icon?

    fun enable() {}
    fun disable() {}

    /**
     * A optional menu item in the Plugins menu
     */
    fun createMenu(menu: JMenu): Boolean = false
}