package objects

object Globals {
    val scenes: HashMap<String, TScene> = HashMap()
    var activeOBSSceneName: String? = null
    var OBSActivityStatus: OBSStatus? = null
    var OBSConnectionStatus: OBSStatus = objects.OBSStatus.UNKNOWN
}