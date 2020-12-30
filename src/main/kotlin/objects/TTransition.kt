package objects

import java.util.logging.Logger

class TTransition {
    private val logger = Logger.getLogger(TTransition::class.java.name)
    var name = ""

    constructor(name: String?) {
        this.name = name ?: ""
    }

    override fun toString(): String = name
}
