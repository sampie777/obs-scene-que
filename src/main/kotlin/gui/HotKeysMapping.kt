package gui

import java.awt.event.KeyEvent

enum class HotKeysMapping(val key: Int) {
    APPLICATION_MENU(KeyEvent.VK_A),
    QUEUE_MENU(KeyEvent.VK_Q),
    PLUGIN_MENU(KeyEvent.VK_U),

    FILE_NEW_ITEM(KeyEvent.VK_N),
    FILE_SAVE_AS_ITEM(KeyEvent.VK_S),
    FILE_OPEN_ITEM(KeyEvent.VK_O),
    
    CONTROL_FRAME_ITEM(KeyEvent.VK_C),
    NOTIFICATIONS_ITEM(KeyEvent.VK_N),
    SCANNER_ITEM(KeyEvent.VK_W),
    SETTINGS_ITEM(KeyEvent.VK_S),
    FULLSCREEN_ITEM(KeyEvent.VK_F),
    INFO_ITEM(KeyEvent.VK_I),
    QUIT_ITEM(KeyEvent.VK_Q),

    PREVIOUS_QUEUE_ITEM(KeyEvent.VK_P),
    NEXT_QUEUE_ITEM(KeyEvent.VK_N),
    SAVE_BUTTON(KeyEvent.VK_S),
    CANCEL_BUTTON(KeyEvent.VK_C),

    WEBSOCKET_SCAN_BUTTON(KeyEvent.VK_S),
    WEBSOCKET_SAVE_BUTTON(KeyEvent.VK_UNDEFINED),
}