package plugins.obs.gui


import GUI
import gui.Refreshable
import handles.QueItemTransferHandler
import objects.OBSState
import objects.que.Que
import objects.que.QueItem
import plugins.obs.ObsPlugin
import plugins.obs.queItems.ObsToggleSourceVisibilityQueItem
import plugins.obs.queItems.SourceVisibilityState
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class ToggleSourceVisibilityFormPanel(private val plugin: ObsPlugin) : JPanel(), Refreshable {
    private val logger = Logger.getLogger(ToggleSourceVisibilityFormPanel::class.java.name)

    val sceneNameList = JComboBox<String>()
    val sourceNameList = JComboBox<String>()

    init {
        initGui()

        GUI.register(this)
    }

    private fun initGui() {
        layout = BorderLayout(5, 5)
        border = CompoundBorder(
            CompoundBorder(
                EmptyBorder(5, 0, 5, 0),
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color(180, 180, 180))
            ),
            EmptyBorder(8, 10, 10, 10)
        )

        val formPanel = formPanel()
        formPanel.isVisible = false

        val addButton = JButton("Show")
        addButton.toolTipText = "Show/Hide form"
        addButton.addActionListener {
            formPanel.isVisible = !formPanel.isVisible
            addButton.text = if (formPanel.isVisible) "Hide" else "Show"
        }

        val titlePanel = JPanel(BorderLayout(5, 5))
        titlePanel.add(JLabel("Source visibility"), BorderLayout.LINE_START)
        titlePanel.add(addButton, BorderLayout.LINE_END)

        add(titlePanel, BorderLayout.PAGE_START)
        add(formPanel, BorderLayout.CENTER)
    }

    private fun formPanel(): JComponent {
        val panel = JPanel(BorderLayout(5, 5))

        val targetStateList = JComboBox(
            arrayOf(
                SourceVisibilityState.TOGGLE,
                SourceVisibilityState.SHOW,
                SourceVisibilityState.HIDE
            )
        )

        val formPanel = JPanel(GridLayout(0, 1))
        formPanel.add(sceneNameList)
        formPanel.add(sourceNameList)
        formPanel.add(targetStateList)

        val addButton = JButton("+")
        addButton.toolTipText = "Click or drag to add"
        addButton.addActionListener {
            val queItem = createQueItem(
                plugin,
                sceneNameList.selectedItem as String,
                sourceNameList.selectedItem as String,
                targetStateList.selectedItem as SourceVisibilityState
            )

            Que.add(queItem)
            GUI.refreshQueItems()
        }
        addButton.transferHandler = QueItemTransferHandler()
        addButton.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val queItem = createQueItem(
                    plugin,
                    sceneNameList.selectedItem as String,
                    sourceNameList.selectedItem as String,
                    targetStateList.selectedItem as SourceVisibilityState
                )

                val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                transferHandler.queItem = queItem
                transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
            }
        })

        panel.add(formPanel, BorderLayout.CENTER)
        panel.add(addButton, BorderLayout.LINE_END)
        return panel
    }

    private fun createQueItem(
        plugin: ObsPlugin,
        sceneName: String,
        sourceName: String,
        targetState: SourceVisibilityState
    ): QueItem {
        return ObsToggleSourceVisibilityQueItem(
            plugin,
            sceneName,
            sourceName,
            targetState
        )
    }

    override fun refreshScenes() {
        val sceneNames = arrayOf(ObsToggleSourceVisibilityQueItem.CURRENT_SCENE_NAME) + OBSState.scenes.map { it.name }
        sceneNameList.model = DefaultComboBoxModel(sceneNames)

        val sourceNames = OBSState.scenes.flatMap { it.sources.map { source -> source.name } }
            .distinct()
            .sorted()
            .toTypedArray()
        sourceNameList.model = DefaultComboBoxModel(sourceNames)
    }
}