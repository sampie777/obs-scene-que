package objects

import GUI
import java.util.logging.Logger

object Que {

    private val logger = Logger.getLogger(Que::class.java.name)

    private var list: ArrayList<TScene> = ArrayList()
    private var currentIndex: Int = -1

    fun getList(): ArrayList<TScene> {
        return list
    }

    fun previous(): TScene? {
        if (currentIndex <= 0) {
            logger.info("Reached start of que")
            return null
        }
        return list.getOrNull(--currentIndex)
    }

    fun current(): TScene? {
        return list.getOrNull(currentIndex)
    }

    fun currentIndex(): Int {
        return currentIndex
    }

    fun next(): TScene? {
        if (currentIndex >= list.size - 1) {
            logger.info("Reached end of que")
            return null
        }
        return list.getOrNull(++currentIndex)
    }

    fun getAt(index: Int): TScene? {
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

    fun setCurrentSceneByName(name: String) {
        val newIndex = list.map { it.name }
            .indexOf(name)

        currentIndex = newIndex
        GUI.switchedScenes()
    }

    fun setCurrentSceneByIndex(index: Int) {
        currentIndex = index
        if (currentIndex >= list.size) {
            currentIndex = list.size - 1
        } else if (currentIndex < -1) {
            currentIndex = -1
        }
    }

    fun add(scene: TScene) {
        add(list.size, scene)
    }

    fun add(index: Int, scene: TScene) {
        var newIndex = index
        if (index > list.size) {
            newIndex = list.size
        } else if (index < 0) {
            newIndex = 0
        }

        // Prevent adding duplicates
        if (newIndex > 0 && list[newIndex - 1].name == scene.name) {
            return
        }

        list.add(newIndex, scene)

        if (newIndex <= currentIndex) {
            currentIndex++
        }
    }

    fun remove(index: Int): TScene? {
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

        val scene = list.removeAt(fromIndex)

        list.add(newIndex, scene)

        return true
    }

    fun previewPrevious(): TScene? {
        return list.getOrNull(currentIndex - 1)
    }

    fun previewNext(): TScene? {
        return list.getOrNull(currentIndex + 1)
    }

    fun removeInvalidItems() {
        list = list.filter { queScene ->
            Globals.scenes.find { it.name == queScene.name } != null
        } as ArrayList<TScene>
    }
}