package themes

class ThemeWrapper(val internalName: String, val displayName: String, val clazz: Class<*>) {
    override fun toString(): String = displayName
}