package objects

import GUI
import config.Config
import mocks.GuiComponentMock
import net.twasi.obsremotejava.objects.Scene
import net.twasi.obsremotejava.objects.Transition
import objects.notifications.Notifications
import kotlin.test.*

class OBSClientTest {

    @BeforeTest
    fun before() {
        OBSState.scenes.clear()
        Notifications.clear()
    }

    @Test
    fun testProcessNewScene() {
        val panelMock = GuiComponentMock()
        GUI.register(panelMock)

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.refreshTransitionsCalled)
        assertFalse(panelMock.switchedScenesCalled)

        // When
        try {
            OBSClient.processNewScene("scene1")
        } catch (e: NullPointerException) {
        }

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.refreshTransitionsCalled)
        assertTrue(panelMock.switchedScenesCalled)
    }

    @Test
    fun testSetOBSScenes() {
        OBSState.clientActivityStatus = OBSClientStatus.LOADING_SCENES
        val panelMock = GuiComponentMock()
        GUI.register(panelMock)

        Config.obsAddress = "ws://somewhereNotLocalhost"

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.refreshTransitionsCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertFalse(panelMock.refreshOBSStatusCalled)

        val scenes = ArrayList<Scene>()
        scenes.add(Scene())
        scenes.add(Scene())
        scenes.add(Scene())

        // When
        OBSClient.processOBSScenesToOBSStateScenes(scenes)

        assertTrue(panelMock.refreshScenesCalled)
        assertFalse(panelMock.refreshTransitionsCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertTrue(panelMock.refreshOBSStatusCalled)
        assertEquals(3, OBSState.scenes.size)
        assertNull(OBSState.clientActivityStatus)
    }

    @Test
    fun testSetOBSTransitions() {
        OBSState.clientActivityStatus = OBSClientStatus.LOADING_TRANSITIONS
        val panelMock = GuiComponentMock()
        GUI.register(panelMock)

        Config.obsAddress = "ws://somewhereNotLocalhost"

        assertFalse(panelMock.refreshScenesCalled)
        assertFalse(panelMock.refreshTransitionsCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertFalse(panelMock.refreshOBSStatusCalled)

        val transitions = ArrayList<Transition>()
        transitions.add(Transition())
        transitions.add(Transition())

        // When
        OBSClient.processOBSTransitionsToOBSStateTransitions(transitions)

        assertFalse(panelMock.refreshScenesCalled)
        assertTrue(panelMock.refreshTransitionsCalled)
        assertFalse(panelMock.switchedScenesCalled)
        assertTrue(panelMock.refreshOBSStatusCalled)
        assertEquals(2, OBSState.transitions.size)
        assertNull(OBSState.clientActivityStatus)
    }

    @Test
    fun testPreregisterCallbacks() {
        OBSClient.clearPreregisteredCallbacks()
        OBSClient.preregisterCallback("mycallback") {
            /* this will fail because the controller argrument will be null which is not permitted */
        }

        OBSClient.registerPreregisteredCallbacks()

        assertTrue(Notifications.list[0].message.contains("mycallback"))
    }
}