package plugins.tallyLight.websocketEvents


import com.google.gson.annotations.SerializedName
import net.twasi.obsremotejava.requests.ResponseBase

class SourceOrderChanged : ResponseBase() {
    @SerializedName("scene-name")
    val sceneName: String? = null
    @SerializedName("scene-items")
    val sceneItems: List<SceneItem> = emptyList()
}

class SceneItem {
    @SerializedName("source-name")
    val sourceName: String? = null
    @SerializedName("item-id")
    val itemId: Int? = null
}