package gui.menu

import javax.swing.JMenuBar

class MenuBar : JMenuBar() {
    init {
        add(ApplicationMenu())
        add(QueueMenu())
        add(PluginMenu())
    }
}