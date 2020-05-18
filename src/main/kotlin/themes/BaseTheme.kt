package themes

import java.awt.Color

abstract class BaseTheme {
    open val FONT_FAMILY = "Dialog"
    open val FONT_COLOR = Color(51, 51, 51)
    open val LINK_FONT_COLOR = Color(25, 90, 244)
    open val BACKGROUND_COLOR = Color(238, 238, 238)
    open val TEXT_FIELD_BACKGROUND_COLOR = Color(255, 255, 255)
    open val BUTTON_BACKGROUND_COLOR = Color(221, 232, 243)
    open val TABLE_HEADER_BACKGROUND_COLOR = Color(238, 238, 238)
    open val TABLE_BACKGROUND_COLOR = Color(255, 255, 255)

    open val BORDER_COLOR = Color(168, 168, 168)
    open val MENU_BAR_BORDER_COLOR = Color(204, 204, 204)

    open val LIST_SELECTION_FONT_COLOR_LIGHT = Color(200, 200, 200)
    open val LIST_SELECTION_FONT_COLOR_DARK = Color(51, 51, 51)
    open val LIST_SELECTION_BACKGROUND_COLOR = Color(184, 207, 229)
    open val LIST_SELECTION_FONT_COLOR_DEFAULT = LIST_SELECTION_FONT_COLOR_DARK

    open val QUE_LIST_BACKGROUND_COLOR = Color(255, 255, 255)
    open val ACTIVE_OBS_COLOR = Color(200, 230, 255)
    open val ACTIVE_OBS_SELECTED_COLOR = Color(180, 200, 235)
    open val ACTIVE_QUE_COLOR = Color(225, 253, 225)
    open val ACTIVE_QUE_SELECTED_COLOR = Color(100, 225, 100)
    open val ACTIVE_QUE_AND_OBS_COLOR = Color(180, 255, 180)
    open val ACTIVE_QUE_AND_OBS_SELECTED_COLOR = Color(100, 225, 100)
    open val NON_EXISTING_COLOR = Color(204, 204, 204)
    open val NON_EXISTING_SELECTED_COLOR = Color(190, 190, 190)

    open val NOTIFICATIONS_BUTTON_ICON_DEFAULT = "/notification-bell-empty-24.png"
    open val NOTIFICATIONS_BUTTON_ICON_ALERT ="/notification-bell-yellow-24.png"
}