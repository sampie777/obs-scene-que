package themes

import java.awt.Color

open class DarkTheme : BaseTheme() {
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

    override val SELECTED_COLOR = Color(184, 207, 229)
    override val ACTIVE_OBS_COLOR = Color(200, 230, 255)
    override val ACTIVE_OBS_SELECTED_COLOR = Color(180, 200, 235)
    override val ACTIVE_QUE_COLOR = Color(225, 253, 225)
    override val ACTIVE_QUE_SELECTED_COLOR = Color(100, 225, 100)
    override val ACTIVE_QUE_AND_OBS_COLOR = Color(180, 255, 180)
    override val ACTIVE_QUE_AND_OBS_SELECTED_COLOR = Color(100, 225, 100)
    override val NON_EXISTING_COLOR = Color(230, 230, 230)
    override val NON_EXISTING_SELECTED_COLOR = Color(190, 190, 190)

    override val NOTIFICATIONS_BUTTON_ICON_DEFAULT = "/notification-bell-empty-inverted-24.png"
    override val NOTIFICATIONS_BUTTON_ICON_ALERT ="/notification-bell-yellow-inverted-24.png"
}