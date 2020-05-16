package plugins.easyworship

import gui.utils.createImageIcon
import handles.QueItemTransferHandler
import plugins.common.BasePlugin
import plugins.common.QueItem
import plugins.easyworship.queItems.*
import java.awt.*
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

@Suppress("unused")
class EasyWorshipPlugin : BasePlugin {
    override val name = "EasyWorshipPlugin"
    override val description = "Que items for integration with EasyWorship"

    override val icon: Icon? = createImageIcon("/plugins/easyworship/icon-14.png")

    override val tabName = "EasyWorship"

    override fun sourcePanel(): JComponent {
        val panel = JPanel(BorderLayout(10, 10))
        panel.border = EmptyBorder(10, 10, 0, 10)

        val titleLabel = JLabel("Actions")
        panel.add(titleLabel, BorderLayout.PAGE_START)

        val queItems = arrayOf(
            EasyWorshipPreviousSongQueItem(this),
            EasyWorshipNextSongQueItem(this),
            EasyWorshipPreviousVerseQueItem(this),
            EasyWorshipNextVerseQueItem(this),
            EasyWorshipLogoScreenQueItem(this),
            EasyWorshipBlackScreenQueItem(this),
            EasyWorshipClearScreenQueItem(this)
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
            "Previous verse" -> EasyWorshipPreviousVerseQueItem(this)
            "Next verse" -> EasyWorshipNextVerseQueItem(this)
            "Previous song" -> EasyWorshipPreviousSongQueItem(this)
            "Next song" -> EasyWorshipNextSongQueItem(this)
            "Toggle logo screen" -> EasyWorshipLogoScreenQueItem(this)
            "Toggle black screen" -> EasyWorshipBlackScreenQueItem(this)
            "Toggle clear screen" -> EasyWorshipClearScreenQueItem(this)
            else -> throw IllegalArgumentException("Invalid EasyWorship Que Item: $value")
        }
    }
}