package objects

object OBSState {
    var currentSceneName: String? = null
    val scenes: ArrayList<TScene> = ArrayList()

    var clientActivityStatus: OBSClientStatus? = null
    var connectionStatus: OBSClientStatus = OBSClientStatus.UNKNOWN
}