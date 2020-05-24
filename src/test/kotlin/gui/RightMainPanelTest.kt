package gui

import config.Config
import mocks.MockPluginDetailPanel
import mocks.MockPluginDetailPanel2
import objects.notifications.Notifications
import plugins.PluginLoader
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.JTabbedPane
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RightMainPanelTest {

    @BeforeTest
    fun before() {
        PluginLoader.detailPanelPlugins.clear()
        Notifications.clear()
        Config.detailPanelLastOpenedTab = ""
    }

    @Test
    fun testWithoutPluginsShowsOnlyActionPanel() {
        val panel = RightMainPanel()

        assertEquals(1, panel.componentCount)
        assertTrue(panel.components[0] is MainPanelControlPanel)
    }

    @Test
    fun testWithOnePluginsShowsSplitPaneWithOnlyDetailPanel() {
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel())

        val panel = RightMainPanel()

        assertEquals(1, panel.componentCount)
        assertTrue(panel.components[0] is JSplitPane)
        assertTrue((panel.components[0] as JSplitPane).topComponent is MainPanelControlPanel)
        assertTrue((panel.components[0] as JSplitPane).bottomComponent is JPanel)
    }

    @Test
    fun testWithMultiplePluginsShowsSplitPaneWitTabPanel() {
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel())
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel2())

        val panel = RightMainPanel()

        assertEquals(1, panel.componentCount)
        assertTrue(panel.components[0] is JSplitPane)
        assertTrue((panel.components[0] as JSplitPane).topComponent is MainPanelControlPanel)
        assertTrue((panel.components[0] as JSplitPane).bottomComponent is JTabbedPane)
        val tabbedPane = (panel.components[0] as JSplitPane).bottomComponent as JTabbedPane
        assertEquals(2, tabbedPane.componentCount)
        assertEquals("MockPluginDetailPanelTabName", tabbedPane.getTitleAt(0))
        assertEquals("MockPluginDetailPanel2TabName", tabbedPane.getTitleAt(1))
    }

    @Test
    fun testWithOnePluginClosesWillNotSaveCurrentSelectedTabName() {
        Config.detailPanelLastOpenedTab = "x"
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel())
        val panel = RightMainPanel()

        panel.windowClosing(null)

        assertEquals("x", Config.detailPanelLastOpenedTab)
    }

    @Test
    fun testWithMultiplePluginsClosesWillSaveCurrentSelectedTabName() {
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel())
        PluginLoader.detailPanelPlugins.add(MockPluginDetailPanel2())
        val panel = RightMainPanel()
        val tabbedPane = (panel.components[0] as JSplitPane).bottomComponent as JTabbedPane

        tabbedPane.selectedIndex = 0
        panel.windowClosing(null)
        assertEquals("MockPluginDetailPanelTabName", Config.detailPanelLastOpenedTab)

        tabbedPane.selectedIndex = 1
        panel.windowClosing(null)
        assertEquals("MockPluginDetailPanel2TabName", Config.detailPanelLastOpenedTab)
    }
}