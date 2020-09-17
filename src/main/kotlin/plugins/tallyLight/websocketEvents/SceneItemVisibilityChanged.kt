package plugins.tallyLight.websocketEvents


import com.google.gson.annotations.SerializedName
import net.twasi.obsremotejava.requests.ResponseBase

class SceneItemVisibilityChanged : ResponseBase() {
    @SerializedName("scene-name")
    val sceneName: String? = null
    @SerializedName("item-name")
    val itemName: String? = null
    @SerializedName("item-id")
    val itemId: Int? = null
    @SerializedName("item-visible")
    val itemVisible: Boolean? = null
}