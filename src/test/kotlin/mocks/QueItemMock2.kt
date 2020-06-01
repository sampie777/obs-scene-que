package mocks

import objects.que.QueItem
import plugins.common.BasePlugin
import java.awt.Color
import javax.swing.JLabel

class QueItemMock2(override val plugin: BasePlugin, override val name: String) : QueItem {
    var isActivated = false
    var isActivatedAsPrevious = false
    var isDeactivated = false
    var isDeactivatedAsPrevious = false
    var cellRendererIsCalled = false
    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = Color(0, 100, 200)

    override fun activate() {
        isActivated = true
    }

    override fun deactivate() {
        isDeactivated = true
    }

    override fun activateAsPrevious() {
        isActivatedAsPrevious = true
    }

    override fun deactivateAsPrevious() {
        isDeactivatedAsPrevious = true
    }

    override fun toConfigString(): String {
        return name
    }

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        cellRendererIsCalled = true
        super.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)
    }

    override fun toString(): String = "QueItemMock2[name=$name]"
}