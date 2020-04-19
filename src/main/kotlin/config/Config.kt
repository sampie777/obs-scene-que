package config

import objects.Que
import objects.TScene

object Config {
    var obsAddress: String = "ws://localhost:4444"
    var obsConnectionDelay: Long = 1000

    var queSceneNames: ArrayList<String> = ArrayList()

    fun load() {
        PropertyLoader.load()
        PropertyLoader.loadConfig(this::class.java)

        Que.clear()
        queSceneNames.forEach {
            val scene = TScene()
            scene.name = it
            Que.add(scene)
        }
    }

    fun save() {
        queSceneNames.clear()
        Que.getList().forEach {
            queSceneNames.add(it.name)
        }

        PropertyLoader.saveConfig(this::class.java)
        PropertyLoader.save()
    }
}