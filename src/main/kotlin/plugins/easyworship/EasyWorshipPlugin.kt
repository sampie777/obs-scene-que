package plugins.easyworship

import handles.QueItemTransferHandler
import plugins.common.BasePlugin
import plugins.common.EmptyQueItem
import plugins.common.QueItem
import plugins.easyworship.queItems.EasyWorshipNextVerseQueItem
import plugins.easyworship.queItems.EasyWorshipPreviousVerseQueItem
import plugins.easyworship.queItems.EasyWorshipQueItem
import java.awt.*
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

@Suppress("unused")
class EasyWorshipPlugin : BasePlugin {
    override val name = "EasyWorshipPlugin"
    override val description = "Que items for integration with EasyWorship"

    override val tabName = "EasyWorship"

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Actions")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        val queItems = arrayOf(
            EasyWorshipPreviousVerseQueItem(),
            EasyWorshipNextVerseQueItem()
        )

        val list: JList<EasyWorshipQueItem> = JList(queItems)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.dragEnabled = true
        list.transferHandler = QueItemTransferHandler()
        list.background = null
        list.font = Font("Dialog", Font.PLAIN, 14)
        list.cursor = Cursor(Cursor.HAND_CURSOR)
        list.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(180, 180, 180)),
            EmptyBorder(10, 10, 0, 10)
        )

        val scrollPanel = JScrollPane(list)
        scrollPanel.preferredSize = Dimension(300, 500)
        scrollPanel.border = null
        panel.add(scrollPanel, BorderLayout.CENTER)

        return panel
    }

    override fun configStringToQueItem(value: String): QueItem {
        return when (value) {
            "Previous verse" -> EasyWorshipPreviousVerseQueItem()
            "Next verse" -> EasyWorshipNextVerseQueItem()
            else -> EmptyQueItem("Invalid EasyWorship Que Item")
        }
    }
}