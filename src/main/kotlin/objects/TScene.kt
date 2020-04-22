package objects

import java.util.*
import java.util.logging.Logger


class TScene {
    private val logger = Logger.getLogger(TScene::class.java.name)
    var name = ""
    var sources: List<TSource> = ArrayList()

    constructor()

    constructor(name: String?) {
        this.name = name ?: ""
    }

    override fun toString(): String = name
}
