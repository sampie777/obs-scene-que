package config

import getCurrentJarDirectory
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Modifier
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap

object PropertyLoader {
    private val logger = Logger.getLogger(PropertyLoader.toString())

    private val userPropertiesFile = File(getCurrentJarDirectory(this).absolutePath + File.separatorChar + "user.properties")
    private var userProperties = Properties()

    private const val sceneValuePairDelimiter = "%=>"
    private const val sceneValuesDelimiter = "%;;"

    fun load() {
        loadUserProperties()
    }

    fun getPropertiesFile(): File {
        return userPropertiesFile
    }

    private fun loadUserProperties() {
        logger.info("Loading user properties from file: " + userPropertiesFile.absolutePath)

        if (!userPropertiesFile.exists()) {
            logger.info("Creating file: " + userPropertiesFile.absolutePath)
            userPropertiesFile.createNewFile()
        }

        val userProperties = Properties()

        FileInputStream(userPropertiesFile).use { fileInputStream -> userProperties.load(fileInputStream) }

        PropertyLoader.userProperties = userProperties
    }

    fun save() {
        saveUserPropertiesToFIle()
    }

    private fun saveUserPropertiesToFIle() {
        logger.info("Saving user properties")

        if (!userPropertiesFile.exists()) {
            logger.info("Creating file: " + userPropertiesFile.absolutePath)
            userPropertiesFile.createNewFile()
        }

        FileOutputStream(userPropertiesFile).use { fileOutputStream ->
            userProperties.store(
                fileOutputStream,
                "User properties for OBS Websocket Client"
            )
        }
    }

    fun loadConfig(configClass: Class<*>) {
        try {
            for (field in configClass.declaredFields) {
                if (field.name == "INSTANCE") {
                    continue
                }

                logger.fine("Loading config field: ${field.name}")

                try {
                    if (!Modifier.isStatic(field.modifiers)) {
                        continue
                    }

                    field.isAccessible = true
                    field.set(null, getValue(userProperties, field.name, field.type))

                } catch (e: IllegalArgumentException) {
                    logger.warning(e.toString())
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error loading configuration: $e", e)
        }
    }

    fun saveConfig(configClass: Class<*>) {
        try {
            for (field in configClass.declaredFields) {
                if (field.name == "INSTANCE") {
                    continue
                }

                try {
                    if (!Modifier.isStatic(field.modifiers)) {
                        continue
                    }

                    field.isAccessible = true
                    val configValue = field.get(Config)

                    logger.fine("Saving config field: ${field.name} with value: $configValue")
                    setPropertyValue(userProperties, field.name, field.type, configValue)

                } catch (e: IllegalArgumentException) {
                    logger.warning(e.toString())
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error saving configuration: $e", e)
        }
    }

    private fun getValue(props: Properties, name: String, type: Class<*>): Any? {
        val value = props.getProperty(name) ?: throw IllegalArgumentException("Missing configuration value: $name")

        if (type == String::class.java) return value
        if (type == Boolean::class.javaPrimitiveType) return java.lang.Boolean.parseBoolean(value)
        if (type == Int::class.javaPrimitiveType) return value.toInt()
        if (type == Float::class.javaPrimitiveType) return value.toFloat()
        if (type == Long::class.javaPrimitiveType) return value.toLong()
        if (type == Double::class.javaPrimitiveType) return value.toDouble()
        if (type == Color::class.java) {
            val rgb = value.split(",")
            return Color(rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
        }
        if (type == HashMap::class.java) {
            if (value.isEmpty()) {
                return HashMap<String, Int>()
            }
            return value.split(sceneValuesDelimiter)
                .map {
                    val v = it.split(sceneValuePairDelimiter);
                    v[0] to v[1].toInt()
                }
                .toMap()
        }
        if (type == ArrayList::class.java) {
            if (value.isEmpty()) {
                return ArrayList<String>()
            }
            return value.split(sceneValuesDelimiter)
        }
        throw IllegalArgumentException("Unknown configuration value type: " + type.name)
    }

    private fun setPropertyValue(props: Properties, name: String, type: Class<*>, value: Any) {
        if (type == Color::class.java) {
            val color = value as Color
            val stringValue = listOf(color.red, color.green, color.blue).joinToString(",")
            props.setProperty(name, stringValue)
            return
        }
        if (type == HashMap::class.java) {
            val hashmap = value as HashMap<*, *>
            val stringValue = hashmap.entries.stream()
                .map { (key, v) -> "$key$sceneValuePairDelimiter$v" }
                .toArray()
                .joinToString(sceneValuesDelimiter)

            props.setProperty(name, stringValue)
            return
        }
        if (type == ArrayList::class.java) {
            val list = value as ArrayList<*>
            val stringValue = list.joinToString(sceneValuesDelimiter)

            props.setProperty(name, stringValue)
            return
        }

        props.setProperty(name, value.toString())
    }
}