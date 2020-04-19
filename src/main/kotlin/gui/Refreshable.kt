package gui

interface Refreshable {
    fun refreshTimer() {}
    fun switchedScenes() {}
    fun refreshScenes() {}
    fun refreshQueScenes() {}

    fun refreshOBSStatus() {}
}