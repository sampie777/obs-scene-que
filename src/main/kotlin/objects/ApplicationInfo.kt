package objects

import gui.mainFrame.MainFrame
import java.util.*

object ApplicationInfo {
    private val properties = Properties()
    val version: String
    val artifactId: String
    val name: String
    val author: String
    val url: String

    init {
        properties.load(MainFrame::class.java.getResourceAsStream("/application.properties"))
        version = properties.getProperty("version")
        artifactId = properties.getProperty("artifactId")
        name = properties.getProperty("name")
        author = properties.getProperty("author")
        url = properties.getProperty("url")
    }

}