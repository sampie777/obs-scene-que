package plugins.tallyLight.websocketEvents

import net.twasi.obsremotejava.requests.ResponseBase


class SourceDestroyed : ResponseBase() {
    val sourceName: String? = null
    val sourceType: String? = null
    val sourceKind: String? = null
}