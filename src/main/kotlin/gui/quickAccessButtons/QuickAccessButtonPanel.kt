package gui.quickAccessButtons

import config.Config
import objects.que.QueLoader
import plugins.common.QueItem
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JPanel

class QuickAccessButtonPanel : JPanel() {

    init {
        initGui()
    }

    private fun initGui() {
        layout = FlowLayout(FlowLayout.LEFT, 15, 20)
        alignmentX = Component.LEFT_ALIGNMENT
        minimumSize = Dimension(10, 10)

        for (index in 0 until Config.quickAccessButtonCount) {
            val queItem = getStringQueItemFromQue(Config.quickAccessButtonQueItems, index)
            add(QuickAccessButton(index, queItem))
        }
    }

    fun getStringQueItemFromQue(stringSourceList: ArrayList<String>, index: Int): QueItem? {
        if (stringSourceList.size - 1 < index) {
            stringSourceList.add("")
        }
        return QueLoader.loadQueItemForStringLine(stringSourceList[index])
    }
}