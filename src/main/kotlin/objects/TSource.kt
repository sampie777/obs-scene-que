package objects

import java.util.*


class TSource {
    var name = ""
    var type = "unknown"
    var settings: Map<String, Any> = HashMap()
    var fileName = ""
    var videoLength = 0

    constructor()

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, type: String) {
        this.name = name
        this.type = type
    }
}
