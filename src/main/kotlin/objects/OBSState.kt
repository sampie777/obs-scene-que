package objects

object OBSState {
    val scenes: ArrayList<TScene> = ArrayList()
    var currentSceneName: String? = null
    var clientActivityStatus: OBSClientStatus? = null
    var connectionStatus: OBSClientStatus = OBSClientStatus.UNKNOWN
}