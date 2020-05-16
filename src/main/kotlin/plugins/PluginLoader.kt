package plugins

import config.Config
import plugins.common.BasePlugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.logging.Logger
import kotlin.collections.HashSet

object PluginLoader {
    private val logger = Logger.getLogger(PluginLoader.toString())
    private val pluginDirectory = Config.pluginDirectory
    private const val internalPluginDirectory = "/plugins"
    private const val pluginExtensionName = "Plugin.jar"
    private const val pluginEntryFileExtensionName = "Plugin.class"

    // Available plugins
    val plugins = HashSet<BasePlugin>()

    fun loadAll() {
        plugins.clear()

        loadInternalPlugins()
        loadExternalPlugins()

        logger.info("${plugins.size} loaded")
    }

    private fun loadInternalPlugins() {
        val pluginDirectories: Array<File> = getInternalPluginFiles()

        if (pluginDirectories.isEmpty()) {
            logger.info("No internal plugins found")
            return
        }

        val classes = getPluginClassesFromDirectories(pluginDirectories)
        logger.info("Internal plugins found: " + classes.joinToString { it })

        loadInternalClasses(classes)
    }

    private fun loadExternalPlugins() {
        val pluginURLs: Array<URL?> = getExternalPluginFiles()

        if (pluginURLs.isEmpty()) {
            logger.info("No external plugins found")
            return
        }

        val jarUrlClassLoader = URLClassLoader(pluginURLs, this::class.java.classLoader)

        val classNames = getPluginClassNamesFromJar(jarUrlClassLoader)
        logger.info("External plugins found: " + classNames.joinToString { it })

        loadExternalClasses(classNames, jarUrlClassLoader)
    }

    private fun getInternalPluginFiles(): Array<File> {
        val pluginDirectories = File(javaClass.getResource(internalPluginDirectory).file)
            .listFiles { file -> file.isDirectory && file.name != "common" }
            ?: return emptyArray()

        logger.info("Internal plugin files found: " + pluginDirectories.joinToString { it.name })

        return pluginDirectories
    }

    private fun getExternalPluginFiles(): Array<URL?> {
        val pluginFiles = File(pluginDirectory).listFiles { file -> file.name.endsWith(pluginExtensionName) }
            ?: return emptyArray()

        logger.info("External plugin files found: " + pluginFiles.joinToString { it.name })

        return fileArrayToUrlArray(pluginFiles)
    }

    private fun getPluginClassesFromDirectories(pluginDirectories: Array<File>): ArrayList<String> {
        val classes = ArrayList<String>()

        for (file in pluginDirectories) {
            val fileEntries: Array<File>? = file.listFiles()
            if (fileEntries == null) {
                logger.warning("Directory is empty: ${file.absolutePath}")
                continue
            }

            val pluginMainClass: File? =
                fileEntries.find { !it.isDirectory && it.name.endsWith(pluginEntryFileExtensionName) }
            if (pluginMainClass == null) {
                logger.warning("No entry file found in directory: ${file.absolutePath}")
                continue
            }

            val rootPath = File(javaClass.getResource("/").file).absolutePath
            val className = pluginMainClass.absolutePath.substringAfter(rootPath)
                .substringBeforeLast(".class")
                .replace("/", ".")
                .replace('\\', '.')
                .trimStart('.')

            classes.add(className)
        }

        return classes
    }

    private fun getPluginClassNamesFromJar(jarUrlClassLoader: URLClassLoader): ArrayList<String> {
        val classNames = ArrayList<String>()

        for (url in jarUrlClassLoader.urLs) {
            val jarFile = JarFile(url.file)
            val jarFileEntries: Enumeration<JarEntry> = jarFile.entries()

            val pluginMainClass: JarEntry? = jarFileEntries.asSequence()
                .find { !it.isDirectory && it.name.endsWith(pluginEntryFileExtensionName) }
            if (pluginMainClass == null) {
                logger.warning("No entry file found in directory: ${url.path}")
                continue
            }

            val className = pluginMainClass.name
                .substringBeforeLast(".class")
                .replace('/', '.')

            classNames.add(className)
        }

        return classNames
    }

    private fun loadInternalClasses(classes: ArrayList<String>) {
        for (className in classes) {
            logger.info("Loading plugin class: $className")

            try {
                val loadedClass = Class.forName(className)

                val instance = loadedClass.newInstance()
                if (instance !is BasePlugin) {
                    logger.warning("Plugin $className is not a valid Plugin instance")
                    continue
                }

                plugins.add(instance)

            } catch (e: Exception) {
                logger.severe("Failed to load plugin: $className")
                e.printStackTrace()
            }
        }
    }

    private fun loadExternalClasses(classNames: ArrayList<String>, urlClassLoader: URLClassLoader) {
        for (className in classNames) {
            logger.info("Loading plugin class: $className")

            try {
                val loadedClass = urlClassLoader.loadClass(className)

                val instance = loadedClass.newInstance()
                if (instance !is BasePlugin) {
                    logger.warning("Plugin $className is not a valid Plugin instance")
                    continue
                }

                plugins.add(instance)

            } catch (e: Exception) {
                logger.severe("Failed to load plugin: $className")
                e.printStackTrace()
            }
        }
    }

    private fun fileArrayToUrlArray(pluginDirectories: Array<File>): Array<URL?> {
        val pluginURLs: Array<URL?> = arrayOfNulls(pluginDirectories.size)
        for (i in pluginDirectories.indices) {
            pluginURLs[i] = pluginDirectories[i].toURI().toURL()
        }
        return pluginURLs
    }

    fun enableAll() {
        for (plugin in plugins) {
            enable(plugin)
        }
    }

    fun disableAll() {
        for (plugin in plugins) {
            disable(plugin)
        }
    }

    fun enable(plugin: BasePlugin) {
        logger.info("Enabling plugin ${plugin.name}")
        plugin.enable()
    }

    fun disable(plugin: BasePlugin) {
        logger.info("Disabling plugin ${plugin.name}")
        plugin.disable()
    }
}