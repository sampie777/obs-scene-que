package plugins.obs

import GUI
import gui.Refreshable
import gui.utils.DefaultSourcesList
import gui.utils.createImageIcon
import objects.OBSState
import objects.TScene
import objects.que.JsonQue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import plugins.obs.queItems.*
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

@Suppress("unused")
class ObsPlugin : QueItemBasePlugin, Refreshable {
    override val name = "ObsPlugin"
    override val description = "Queue items for integration with OBS"
    override val version: String = "0.0.0"

    override val icon: Icon? = createImageIcon("/plugins/obs/icon-14.png")

    override val tabName = "OBS"

    internal val quickAccessColor = Color(229, 238, 255)
    private val list: JList<QueItem> = DefaultSourcesList()

    override fun enable() {
        super.enable()
        GUI.register(this)
    }

    override fun disable() {
        super.disable()
        GUI.unregister(this)
    }

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Available scenes")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = BorderFactory.createLineBorder(Color(180, 180, 180))
        panel.add(scrollPanel, BorderLayout.CENTER)

        panel.add(createActionPanel(), BorderLayout.PAGE_END)

        return panel
    }

    private fun createActionPanel(): JPanel {
        val panel = JPanel(BorderLayout(10, 10))
        panel.add(JLabel("Other actions"), BorderLayout.PAGE_START)

        val queItems = arrayOf(
            ObsStartStreamingQueItem(this),
            ObsStopStreamingQueItem(this),
            ObsStartRecordingQueItem(this),
            ObsStopRecordingQueItem(this)
        )

        val actionsList: JList<QueItem> = DefaultSourcesList(queItems)
        actionsList.transferHandler = ObsActionQueItemTransferHandler(this)

        val actionListScrollPanel = JScrollPane(actionsList)
        actionListScrollPanel.preferredSize = Dimension(100, 100)
        actionListScrollPanel.border = BorderFactory.createLineBorder(Color(180, 180, 180))
        panel.add(actionListScrollPanel, BorderLayout.CENTER)

        return panel
    }

    override fun configStringToQueItem(value: String): QueItem {
        return ObsSceneQueItem(this, TScene(value))
    }

    override fun jsonToQueItem(jsonQueItem: JsonQue.QueItem): QueItem {
        return when (jsonQueItem.className) {
            ObsSceneQueItem::class.java.simpleName -> ObsSceneQueItem(this, TScene(jsonQueItem.name))
            ObsStartStreamingQueItem::class.java.simpleName -> ObsStartStreamingQueItem(this)
            ObsStopStreamingQueItem::class.java.simpleName -> ObsStopStreamingQueItem(this)
            ObsStartRecordingQueItem::class.java.simpleName -> ObsStartRecordingQueItem(this)
            ObsStopRecordingQueItem::class.java.simpleName -> ObsStopRecordingQueItem(this)
            else -> throw IllegalArgumentException("Invalid OBS Plugin queue item: ${jsonQueItem.className}")
        }
    }

    override fun refreshScenes() {
        list.setListData(OBSState.scenes.map { ObsSceneQueItem(this, it) }.toTypedArray())
        list.repaint()
    }
}