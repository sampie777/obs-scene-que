package mocks

import objects.que.QueItem
import plugins.common.BasePlugin
import javax.swing.JLabel

class QueItemMock(override val plugin: BasePlugin, override val name: String) : QueItem {
    var isActivated = false
    var isDeactivated = false
    var cellRendererIsCalled = false
    override var executeAfterPrevious = false

    override fun activate() {
        isActivated = true
    }

    override fun deactivate() {
        isDeactivated = true
    }

    override fun toConfigString(): String {
        return name
    }

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        cellRendererIsCalled = true
        super.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)
    }

    override fun toString(): String = "QueItemMock[name=$name]"
}