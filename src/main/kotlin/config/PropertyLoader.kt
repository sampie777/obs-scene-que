package config

import com.google.gson.Gson
import getCurrentJarDirectory
import jsonBuilder
import objects.json.NativeKeyEventJson
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Modifier
import java.util.*
import java.util.logging.Logger
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object PropertyLoader {
    private val logger = Logger.getLogger(PropertyLoader.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private val userPropertiesFile = File(
        getCurrentJarDirectory(this).absolutePath + File.separatorChar + "obs-scene-que.properties"
    )
    private var userProperties = Properties()

    private const val sceneValuePairDelimiter = "%=>"
    private const val sceneValuesDelimiter = "%;;"
    private const val defaultValueDelimiter = ","

    fun load() {
        loadUserProperties()
    }

    fun getPropertiesFile(): File {
        return userPropertiesFile
    }

    fun getUserProperties(): Properties {
        return userProperties
    }

    private fun loadUserProperties() {
        logger.info("Loading user properties from file: " + userPropertiesFile.absolutePath)

        if (createNewPropertiesFile()) {
            return
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

        if (!writeToFile) {
            logger.info("writeToFile is turned off, so not saving properties to file")
            return
        }

        createNewPropertiesFile()

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
                if (field.name == "INSTANCE" || field.name == "logger") {
                    continue
                }

                logger.fine("Loading config field: ${field.name}")

                try {
                    if (!Modifier.isStatic(field.modifiers)) {
                        continue
                    }

                    field.isAccessible = true

                    val propertyValue = userProperties.getProperty(field.name)
                        ?: throw IllegalArgumentException("Missing configuration value: ${field.name}")

                    field.set(null, stringToTypedValue(propertyValue, field.name, field.type))

                } catch (e: IllegalArgumentException) {
                    logger.warning(e.toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error loading configuration: $e", e)
        }
    }

    /**
     * Populates the userProperties object with the values from the given Config object.
     * Returns true if the values have changed, otherwise returns false
     */
    fun saveConfig(configClass: Class<*>): Boolean {
        val newProperties = Properties()

        try {
            for (field in configClass.declaredFields) {
                if (field.name == "INSTANCE" || field.name == "logger") {
                    continue
                }

                try {
                    if (!Modifier.isStatic(field.modifiers)) {
                        continue
                    }

                    field.isAccessible = true
                    val configValue = field.get(Config)

                    logger.finer("Saving config field: ${field.name} with value: $configValue")
                    typedValueToPropertyValue(newProperties, field.name, field.type, configValue)

                } catch (e: IllegalArgumentException) {
                    logger.warning(e.toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Error saving configuration: $e", e)
        }

        if (userProperties == newProperties) {
            return false
        }
        userProperties = newProperties
        return true
    }

    fun stringToTypedValue(value: String, name: String, type: Class<*>): Any? {
        when (type) {
            String::class.java -> return value
            Boolean::class.javaPrimitiveType -> return java.lang.Boolean.parseBoolean(value)
            Int::class.javaPrimitiveType -> return value.toInt()
            Float::class.javaPrimitiveType -> return value.toFloat()
            Long::class.javaPrimitiveType -> return value.toLong()
            Double::class.javaPrimitiveType -> return value.toDouble()
            Color::class.java -> {
                val rgb = value.split(defaultValueDelimiter)
                if (rgb.size < 3) {
                    throw IllegalArgumentException("Configuration parameter '$name' has invalid value: $value")
                }
                return Color(rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
            }
            HashMap::class.java -> {
                if (value.isEmpty()) {
                    return HashMap<String, Int>()
                }
                return value.split(sceneValuesDelimiter)
                    .map {
                        val pair: List<String> = it.split(sceneValuePairDelimiter)
                        if (pair.size != 2) {
                            logger.warning("Invalid property pair: $it")
                        }
                        pair
                    }
                    .filter { it.size == 2 }
                    .map { it[0] to it[1].toInt() }
                    .toMap(HashMap())
            }
            ArrayList::class.java -> {
                if (value.isEmpty()) {
                    return ArrayList<String>()
                }
                return value.split(sceneValuesDelimiter)
            }
            Point::class.java -> {
                val values = value.split(defaultValueDelimiter)
                if (values.size != 2) {
                    throw IllegalArgumentException("Configuration parameter '$name' has invalid value: $value")
                }
                return Point(values[0].toInt(), values[1].toInt())
            }
            Dimension::class.java -> {
                val values = value.split(defaultValueDelimiter)
                if (values.size != 2) {
                    throw IllegalArgumentException("Configuration parameter '$name' has invalid value: $value")
                }
                return Dimension(values[0].toInt(), values[1].toInt())
            }
            NativeKeyEventJson::class.java -> {
                return NativeKeyEventJson.fromJson(value)
            }
            HashSet::class.java -> {
                return Gson().fromJson(value, HashSet::class.java)
            }
            else -> throw IllegalArgumentException("Unknown configuration value type: " + type.name)
        }

    }

    private fun typedValueToPropertyValue(props: Properties, name: String, type: Class<*>, value: Any?) {
        if (value == null) {
            props.setProperty(name, "")
            return
        }

        val stringValue = when (type) {
            Color::class.java -> {
                val color = value as Color
                listOf(color.red, color.green, color.blue).joinToString(defaultValueDelimiter)
            }
            HashMap::class.java -> {
                val hashmap = value as HashMap<*, *>
                hashmap.entries.stream()
                    .map { (key, v) -> "$key$sceneValuePairDelimiter$v" }
                    .toArray()
                    .joinToString(sceneValuesDelimiter)
            }
            ArrayList::class.java -> {
                val list = value as ArrayList<*>
                list.joinToString(sceneValuesDelimiter)
            }
            Point::class.java -> {
                val point = value as Point
                point.x.toString() + defaultValueDelimiter + point.y
            }
            Dimension::class.java -> {
                val dimension = value as Dimension
                dimension.width.toString() + defaultValueDelimiter + dimension.height
            }
            NativeKeyEventJson::class.java -> {
                (value as NativeKeyEventJson).toJson()
            }
            HashSet::class.java -> {
                jsonBuilder(prettyPrint = false).toJson(value)
            }
            else -> value.toString()
        }

        props.setProperty(name, stringValue)

    }

    private fun createNewPropertiesFile(): Boolean {
        if (userPropertiesFile.exists()) {
            return false
        }

        if (!writeToFile) {
            logger.info("writeToFile is turned off, so not creating a new file")
            return true
        }

        logger.info("Creating file: " + userPropertiesFile.absolutePath)
        userPropertiesFile.createNewFile()
        return true
    }
}