package themes

import java.awt.Color

class DarkTheme : BaseTheme() {
    override val FONT_FAMILY = "Dialog"
    override val FONT_COLOR = Color(200, 200, 200)
    override val LINK_FONT_COLOR = Color(106, 149, 239)
    override val BACKGROUND_COLOR = Color(50, 50, 50)
    override val TEXT_FIELD_BACKGROUND_COLOR = Color(78, 78, 78)
    override val BUTTON_BACKGROUND_COLOR = Color(65, 65, 65)
    override val TABLE_HEADER_BACKGROUND_COLOR = Color(50, 50, 50)
    override val TABLE_BACKGROUND_COLOR = Color(78, 78, 78)

    override val BORDER_COLOR = Color(130, 130, 130)
    override val MENU_BAR_BORDER_COLOR = Color(65, 65, 65)

    override val QUE_LIST_BACKGROUND_COLOR = Color(60, 60, 60)
    override val LIST_SELECTION_FONT_COLOR_LIGHT = Color(200, 200, 200)
    override val LIST_SELECTION_FONT_COLOR_DARK = Color(51, 51, 51)
    override val LIST_SELECTION_BACKGROUND_COLOR = Color(79, 84, 90)
    override val LIST_SELECTION_FONT_COLOR_DEFAULT = LIST_SELECTION_FONT_COLOR_DARK

    override val ACTIVE_OBS_COLOR = Color(70, 79, 89)
    override val ACTIVE_OBS_SELECTED_COLOR = Color(85, 95, 107)
    override val ACTIVE_QUE_COLOR = Color(80, 89, 80)
    override val ACTIVE_QUE_SELECTED_COLOR = Color(96, 107, 96)
    override val ACTIVE_QUE_AND_OBS_COLOR = Color(129, 179, 129)
    override val ACTIVE_QUE_AND_OBS_SELECTED_COLOR = Color(147, 204, 147)
    override val NON_EXISTING_COLOR = Color(97, 94, 85)
    override val NON_EXISTING_SELECTED_COLOR = Color(148, 144, 130)

    override val NOTIFICATIONS_BUTTON_ICON_DEFAULT = "/notification-bell-empty-inverted-24.png"
    override val NOTIFICATIONS_BUTTON_ICON_ALERT ="/notification-bell-yellow-inverted-24.png"
}