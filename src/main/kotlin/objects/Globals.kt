package objects

object Globals {
    val scenes: ArrayList<TScene> = ArrayList()
    var activeOBSSceneName: String? = null
    var OBSActivityStatus: OBSStatus? = null
    var OBSConnectionStatus: OBSStatus = objects.OBSStatus.UNKNOWN
}