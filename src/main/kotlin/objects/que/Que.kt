package objects.que

import GUI
import objects.OBSState
import objects.notifications.Notifications
import plugins.common.QueItem
import plugins.obs.ObsSceneQueItem
import java.util.logging.Logger

object Que {

    private val logger = Logger.getLogger(Que.toString())

    private var list: ArrayList<QueItem> = ArrayList()
    private var currentIndex: Int = -1

    fun getList(): ArrayList<QueItem> {
        return list
    }

    fun setList(list: ArrayList<QueItem>) {
        this.list = list
    }

    fun run() {
        val current = current() ?: return

        activateItem(current)
        GUI.refreshQueItems()
    }

    fun previous() {
        if (currentIndex <= 0) {
            logger.info("Reached start of que")
            return
        }
        activateItem(list.getOrNull(--currentIndex))
        GUI.refreshQueItems()
    }

    fun current(): QueItem? {
        return list.getOrNull(currentIndex)
    }

    fun currentIndex(): Int {
        return currentIndex
    }

    fun next() {
        if (currentIndex >= list.size - 1) {
            logger.info("Reached end of que")
            return
        }
        activateItem(list.getOrNull(++currentIndex))
        GUI.refreshQueItems()
    }

    fun getAt(index: Int): QueItem? {
        return list.getOrNull(index)
    }

    fun size(): Int {
        return list.size
    }

    fun isFirstItem(): Boolean {
        return currentIndex <= 0
    }

    fun isLastItem(): Boolean {
        return currentIndex >= list.size - 1
    }

    fun setCurrentQueItemByName(pluginName: String, name: String) {
        val newIndex = list.indexOfFirst { it.name == name && it.plugin.name == pluginName }
        currentIndex = newIndex
        GUI.switchedScenes()
    }

    fun setCurrentQueItemByIndex(index: Int) {
        currentIndex = index
        if (currentIndex >= list.size) {
            currentIndex = list.size - 1
        } else if (currentIndex < -1) {
            currentIndex = -1
        }
    }

    fun add(item: QueItem) {
        add(list.size, item)
    }

    fun add(index: Int, item: QueItem) {
        println("Addind item at: $index")
        var newIndex = index
        if (index > list.size) {
            newIndex = list.size
        } else if (index < 0) {
            newIndex = 0
        }

        // Prevent adding duplicates
        if (newIndex > 0
            && list[newIndex - 1].name == item.name
            && list[newIndex - 1].plugin.name == item.plugin.name) {
            return
        }

        list.add(newIndex, item)

        if (newIndex <= currentIndex) {
            currentIndex++
        }
    }

    fun remove(index: Int): QueItem? {
        if (index < 0 || index >= list.size) {
            return null
        }

        if (index <= currentIndex) {
            currentIndex--
        }

        return list.removeAt(index)
    }

    fun clear() {
        list.clear()
        currentIndex = -1
    }

    fun move(fromIndex: Int, toIndex: Int): Boolean {
        if (fromIndex < 0 || fromIndex >= list.size || toIndex < 0 || toIndex > list.size) {
            return false
        }

        var newIndex = toIndex
        if (toIndex > fromIndex) {
            newIndex = toIndex - 1
        }

        val item = list.removeAt(fromIndex)

        list.add(newIndex, item)

        return true
    }

    fun previewPrevious(): QueItem? {
        return list.getOrNull(currentIndex - 1)
    }

    fun previewNext(): QueItem? {
        return list.getOrNull(currentIndex + 1)
    }

    fun removeInvalidItems() {
        list = list.filter { item ->
            item !is ObsSceneQueItem ||
                    OBSState.scenes.find { it.name == item.name } != null
        } as ArrayList<QueItem>
    }

    private fun activateItem(item: QueItem?) {
        try {
            item!!.activate()
        } catch (e: Exception) {
            logger.warning("Failed to run current que item")
            e.printStackTrace()
            Notifications.add("Failed to run current que item '${item?.name}'", "Que")
        }
    }

    fun load() {
        QueLoader.load()
    }

    fun save() {
        QueLoader.save()
    }

    fun enableWriteToFile(value: Boolean) {
        QueLoader.writeToFile = value
    }
}