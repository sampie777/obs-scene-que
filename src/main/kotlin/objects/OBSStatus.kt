package objects

enum class OBSStatus(val status: String) {
    UNKNOWN("Unknown"),
    CONNECTED("Connected"),
    DISCONNECTED("Disconnected"),
    LOADING_SCENE_SOURCES("Loading scene sources..."),
    CONNECTING("Connecting..."),
    CONNECTION_FAILED("Connection failed!"),
    LOADING_SCENES("Loading scenes..."),
}