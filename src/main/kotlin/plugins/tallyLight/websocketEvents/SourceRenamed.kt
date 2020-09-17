package plugins.tallyLight.websocketEvents

import net.twasi.obsremotejava.requests.ResponseBase


class SourceRenamed : ResponseBase() {
    val previousName: String? = null
    val newName: String? = null
    val sourceType: String? = null
}