package plugins.common

import javax.swing.Icon

interface BasePlugin {

    val name: String
    val description: String
    val version: String

    val icon: Icon?

    fun enable() {}
    fun disable() {}
}