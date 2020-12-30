package objects

enum class OBSClientStatus(val status: String) {
    UNKNOWN("Unknown"),
    CONNECTED("Connected"),
    DISCONNECTED("Disconnected"),
    LOADING_SCENE_SOURCES("Loading scene sources..."),
    LOADING_TRANSITIONS("Loading transitions..."),
    CONNECTING("Connecting..."),
    RECONNECTING("Reconnecting..."),
    CONNECTION_FAILED("Connection failed!"),
    LOADING_SCENES("Loading scenes..."),
}