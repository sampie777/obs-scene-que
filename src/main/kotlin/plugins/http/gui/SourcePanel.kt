package plugins.http.gui


import GUI
import handles.QueItemTransferHandler
import objects.notifications.Notifications
import objects.que.Que
import org.eclipse.jetty.http.HttpMethod
import plugins.http.HttpPlugin
import plugins.http.queItems.HttpQueItem
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder


class SourcePanel(private val plugin: HttpPlugin) : JPanel() {
    private val logger = Logger.getLogger(SourcePanel::class.java.name)

    private val nameField = JTextField("name (optional)")
    private val urlField = JTextField("url")
    private val methodComboBox = JComboBox(
        arrayOf(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE
        )
    )

    init {
        initGui()
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 0, 10)

//        val titleLabel = JLabel("Items")
//        add(titleLabel, BorderLayout.PAGE_START)

        val itemListPanel = JPanel(GridLayout(0, 1))
        itemListPanel.add(createPanelForQueItem())

        val scrollPanelInnerPanel = JPanel(BorderLayout())
        scrollPanelInnerPanel.add(itemListPanel, BorderLayout.PAGE_START)
        val scrollPanel = JScrollPane(scrollPanelInnerPanel)
        scrollPanel.border = null
        add(scrollPanel, BorderLayout.CENTER)
    }

    private fun createPanelForQueItem(): JComponent {
        val panel = JPanel(BorderLayout(5, 5))

        nameField.toolTipText = "Queue item name"
        urlField.toolTipText = "Request URL"

        val addButton = JButton("+")
        addButton.toolTipText = "Click or drag to add"
        addButton.addActionListener {
            val queItem = inputToQueItem() ?: return@addActionListener
            Que.add(queItem)

            GUI.refreshQueItems()
        }
        addButton.transferHandler = QueItemTransferHandler()
        addButton.addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val queItem = inputToQueItem() ?: return

                val transferHandler = (e.source as JButton).transferHandler as QueItemTransferHandler
                transferHandler.queItem = queItem
                transferHandler.exportAsDrag(e.source as JComponent, e, TransferHandler.COPY)
            }
        })

        val textFieldPanel = JPanel(GridLayout(0, 1))
        textFieldPanel.add(nameField)
        textFieldPanel.add(urlField)
        textFieldPanel.add(methodComboBox)

        panel.add(JLabel("New HTTP request"), BorderLayout.PAGE_START)
        panel.add(textFieldPanel, BorderLayout.CENTER)
        panel.add(addButton, BorderLayout.LINE_END)
        return panel
    }

    private fun inputToQueItem(): HttpQueItem? {
        if (methodComboBox.selectedIndex < 0) {
            return null
        }

        val url = urlField.text.trim()
        if (url == "url") {
            return null
        }

        val method = (methodComboBox.selectedItem as HttpMethod).toString()

        var name = nameField.text.trim()
        if (name.isEmpty() || name == "name (optional)") {
           name = "${method}: $url"
        }

        return try {
            HttpQueItem(
                plugin,
                name = name,
                url = url,
                method = method,
                body = null
            )
        } catch (e: Exception) {
            logger.warning("Failed to create Http queue item")
            e.printStackTrace()
            Notifications.add("Failed to create QueItem", "Http Plugin")
            null
        }
    }
}