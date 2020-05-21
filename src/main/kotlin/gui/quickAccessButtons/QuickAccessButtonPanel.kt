package gui.quickAccessButtons

import com.google.gson.Gson
import config.Config
import objects.que.QueLoader
import plugins.common.QueItem
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import java.util.logging.Logger
import javax.swing.JPanel

class QuickAccessButtonPanel : JPanel() {

    private val logger = Logger.getLogger(QuickAccessButtonPanel::class.java.name)

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

        if (stringSourceList[index].isEmpty()) {
            return null
        }

        val jsonQueItem = QueLoader.jsonQueItemFromJson(stringSourceList[index])
        if (jsonQueItem != null) {
            return QueLoader.loadQueItemFromJson(jsonQueItem)
        }

        // Create backwards compatibility
        try {
            val queItem = QueLoader.loadQueItemForStringLine(stringSourceList[index])
            if (queItem != null) {
                Config.quickAccessButtonQueItems[index] = Gson().toJson(queItem.toJson())
                return queItem
            }
        } catch (e: Exception) {
            logger.warning("Also failed to convert QueItem from possible old string format")
            e.printStackTrace()
        }

        // If all else fails
        stringSourceList[index] = ""
        return null
    }
}