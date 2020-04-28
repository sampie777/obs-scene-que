package gui.menu

import javax.swing.JMenuBar

class MenuBar : JMenuBar() {
    init {
        add(ApplicationMenu())
    }
}