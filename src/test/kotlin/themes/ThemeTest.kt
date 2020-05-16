package themes

import config.Config
import mocks.ThemeMock
import java.awt.Color
import javax.swing.UIManager
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeTest {

    @Test
    fun testSetAndApplyTheme() {
        Config.theme = "LightTheme"
        Theme.init()

        // Then
        assertEquals(LightTheme().BACKGROUND_COLOR, UIManager.get("Panel.background"))

        // When
        Config.theme = "DarkTheme"
        Theme.init()

        // Then
        assertEquals(DarkTheme().BACKGROUND_COLOR, UIManager.get("Panel.background"))
    }

    @Test
    fun testSetAndApplyNonExistingThemeDefaultsToDefaultTheme() {
        UIManager.put("Panel.background", Color(0, 100, 200))
        assertEquals(Color(0, 100, 200), UIManager.get("Panel.background"))

        Config.theme = "NonExistingTheme"
        Theme.init()

        // Then
        assertEquals(LightTheme().BACKGROUND_COLOR, UIManager.get("Panel.background"))
    }

    @Test
    fun testAddNewThemeAndSetAndApplyTheme() {
        Config.theme = "LightTheme"
        Theme.init()

        // Then
        assertEquals(2, Theme.availableThemes().size)
        assertEquals(LightTheme().BACKGROUND_COLOR, UIManager.get("Panel.background"))

        // When
        Theme.addTheme("ThemeMock", "Mock Theme", ThemeMock::class.java)
        Config.theme = "ThemeMock"
        Theme.init()

        // Then
        assertEquals(3, Theme.availableThemes().size)
        assertEquals(Color.RED, UIManager.get("Panel.background"))
    }
}