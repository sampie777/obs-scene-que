package objects

import GUI
import GuiComponentMock
import config.Config
import net.twasi.obsremotejava.objects.Scene
import kotlin.test.*

class OBSClientTest {

    @BeforeTest
    fun before() {
        Globals.scenes.clear()
    }

    @Test
    fun testProcessNewScene() {
        val panelMock = GuiComponentMock()
        GUI.register(panelMock)

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.switchedScenesCalled)

        // When
        try {
            OBSClient.processNewScene("scene1")
        } catch (e: NullPointerException) {}

        assertFalse(panelMock.refreshScenesCalled)
        assertTrue(panelMock.switchedScenesCalled)
    }

    @Test
    fun testSetOBSScenes() {
        Globals.OBSActivityStatus = OBSStatus.LOADING_SCENES
        val panelMock = GuiComponentMock()
        GUI.register(panelMock)

        Config.obsAddress = "ws://somewhereNotLocalhost"

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertFalse(panelMock.refreshOBSStatusCalled)

        val scenes = ArrayList<Scene>()
        scenes.add(Scene())
        scenes.add(Scene())
        scenes.add(Scene())

        // When
        OBSClient.setOBSScenes(scenes)

        assertTrue(panelMock.refreshScenesCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertTrue(panelMock.refreshOBSStatusCalled)
        assertEquals(3, Globals.scenes.size)
        assertNull(Globals.OBSActivityStatus)
    }
}