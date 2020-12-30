import gui.Refreshable
import java.awt.Component
import java.util.logging.Logger
import javax.swing.JFrame

object GUI {
    private val logger = Logger.getLogger(GUI::class.java.name)

    var currentFrame: JFrame? = null

    private val components: HashSet<Refreshable> = HashSet()

    fun refreshTimer() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshTimer()
        }
    }

    fun switchedScenes() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.switchedScenes()
        }
    }

    fun refreshScenes() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshScenes()
        }
    }

    fun refreshTransitions() {
        for (component in components.toTypedArray()) {
            component.refreshTransitions()
        }
    }

    fun refreshQueItems() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshQueItems()
        }
    }

    fun refreshQueueName() {
        components.toTypedArray().forEach {
            try {
                it.refreshQueueName()
            } catch (e: AbstractMethodError) {
                logger.severe("Function refreshQueueName is not implemented for component: ${it::class.java}")
            } catch (t: Throwable) {
                logger.severe("Failed to execute refreshQueueName for component: ${it::class.java}")
                t.printStackTrace()
            }
        }
    }

    fun refreshOBSStatus() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshOBSStatus()
        }
    }

    fun refreshNotifications() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshNotifications()
        }
    }

    fun windowClosing(window: Component?) {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.windowClosing(window)
        }
    }


    fun register(component: Refreshable) {
        logger.info("Registering component: ${component::class.java}")
        components.add(component)
    }

    fun isRegistered(component: Refreshable): Boolean {
        return components.contains(component)
    }

    fun unregister(component: Refreshable) {
        logger.info("Unregistering component: ${component::class.java}")
        components.remove(component)
    }

    fun unregisterAll() {
        logger.info("Unregistering all (${components.size}) components")
        components.clear()
    }

    fun registeredComponents() = components
}