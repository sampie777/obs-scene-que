package objects.json


import com.google.gson.Gson
import jsonBuilder
import org.jnativehook.keyboard.NativeKeyEvent
import java.util.logging.Logger

class NativeKeyEventJson(
    val modifiers: Int,
    val rawCode: Int,
    val keyCode: Int,
    val keyChar: Char
) {
    companion object {
        private val logger = Logger.getLogger(NativeKeyEventJson::class.java.name)

        fun fromEvent(e: NativeKeyEvent): NativeKeyEventJson {
            return NativeKeyEventJson(
                e.modifiers,
                e.rawCode,
                e.keyCode,
                e.keyChar
            )
        }

        fun fromJson(json: String): NativeKeyEventJson? {
            return try {
                Gson().fromJson(json, NativeKeyEventJson::class.java)
            } catch (e: Exception) {
                logger.warning("Failed to convert json to NativeKeyEventJson: $json")
                e.printStackTrace()
                null
            }
        }
    }


    fun toEvent(): NativeKeyEvent {
        return NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_RELEASED,
            modifiers,
            rawCode,
            keyCode,
            keyChar
        )
    }

    fun toJson(): String {
        return jsonBuilder(prettyPrint = false).toJson(this)
    }

    fun isEqualTo(e: NativeKeyEvent): Boolean {
        return modifiers == e.modifiers
                && rawCode == e.rawCode
                && keyCode == e.keyCode
                && keyChar == e.keyChar
    }
}