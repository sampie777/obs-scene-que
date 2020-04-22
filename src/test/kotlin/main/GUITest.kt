package main

import GuiComponentMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GUITest {

    @BeforeTest
    fun before() {
        GUI.unregisterAll()
    }

    @Test
    fun testRegisterAndUnregisterComponent() {
        val guiComponentMock = GuiComponentMock()
        assertFalse(GUI.isRegistered(guiComponentMock))

        GUI.register(guiComponentMock)
        assertTrue(GUI.isRegistered(guiComponentMock))

        GUI.unregister(guiComponentMock)
        assertFalse(GUI.isRegistered(guiComponentMock))
    }

    @Test
    fun testSwitchedScenes() {
        val guiComponentMock = GuiComponentMock()
        GUI.register(guiComponentMock)

        GUI.switchedScenes()

        assertTrue(guiComponentMock.switchedScenesCalled)
    }

    @Test
    fun testRefreshScenes() {
        val guiComponentMock = GuiComponentMock()
        GUI.register(guiComponentMock)

        GUI.refreshScenes()

        assertTrue(guiComponentMock.refreshScenesCalled)
    }

    @Test
    fun testRefreshOBSStatus() {
        val guiComponentMock = GuiComponentMock()
        GUI.register(guiComponentMock)

        GUI.refreshOBSStatus()

        assertTrue(guiComponentMock.refreshOBSStatusCalled)
    }
}