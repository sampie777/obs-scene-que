package plugins.obs.gui


import GUI
import gui.Refreshable
import gui.utils.DefaultSourcesList
import objects.OBSState
import objects.que.QueItem
import plugins.obs.ObsActionQueItemTransferHandler
import plugins.obs.ObsPlugin
import plugins.obs.queItems.*
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class SourcePanel(private val plugin: ObsPlugin) : JPanel(), Refreshable {
    private val logger = Logger.getLogger(SourcePanel::class.java.name)

    private val list: JList<QueItem> = DefaultSourcesList()

    init {
        initGui()
        GUI.register(this)

        refreshScenes()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Available scenes")
        add(titleLabel, BorderLayout.PAGE_START)

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = BorderFactory.createLineBorder(Color(180, 180, 180))
        add(scrollPanel, BorderLayout.CENTER)

        add(createCustomFormsPanel(), BorderLayout.PAGE_END)
    }

    private fun createActionPanel(): JPanel {
        val panel = JPanel(BorderLayout(10, 10))
        panel.add(JLabel("Other actions"), BorderLayout.PAGE_START)

        val queItems = arrayOf(
            ObsStartStreamingQueItem(plugin),
            ObsStopStreamingQueItem(plugin),
            ObsStartRecordingQueItem(plugin),
            ObsStopRecordingQueItem(plugin)
        )

        val actionsList: JList<QueItem> = DefaultSourcesList(queItems)
        actionsList.transferHandler = ObsActionQueItemTransferHandler(plugin)

        val actionListScrollPanel = JScrollPane(actionsList)
        actionListScrollPanel.preferredSize = Dimension(100, 100)
        actionListScrollPanel.border = BorderFactory.createLineBorder(Color(180, 180, 180))
        panel.add(actionListScrollPanel, BorderLayout.CENTER)

        return panel
    }

    private fun createCustomFormsPanel(): JComponent {
        val panel = JPanel()
        panel.layout = GroupLayout(panel)
        val layout = panel.layout as GroupLayout
        layout.autoCreateGaps = true

        val actionPanel = createActionPanel()
        val sourceVisibilityFormPanel = ToggleSourceVisibilityFormPanel(plugin)

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup()
                        .addComponent(actionPanel)
                        .addComponent(sourceVisibilityFormPanel)
                )
        )
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(actionPanel)
                .addComponent(sourceVisibilityFormPanel)
        )
        return panel
    }

    override fun refreshScenes() {
        list.setListData(OBSState.scenes.map { ObsSceneQueItem(plugin, it) }.toTypedArray())
        list.repaint()
    }
}