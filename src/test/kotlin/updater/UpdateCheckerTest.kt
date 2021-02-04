package updater

import objects.ApplicationInfo
import objects.notifications.Notifications
import org.junit.Test
import org.mockito.Mockito.*
import java.net.MalformedURLException
import kotlin.test.*

class UpdateCheckerTest {

    @BeforeTest
    fun before() {
        Notifications.clear()
        UpdateChecker().updateLatestKnownVersion(ApplicationInfo.version)
    }

    @Test
    fun `test clearUpdateHistory clears the latest known version`() {
        val updateChecker = UpdateChecker()

        updateChecker.updateLatestKnownVersion("now")
        assertEquals("now", updateChecker.getLatestKnownVersion())

        updateChecker.clearUpdateHistory()
        assertEquals("", updateChecker.getLatestKnownVersion())
    }

    @Test
    fun `test getRemoteTagResponse creates notification when URL is malformed and returns null`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).then { throw MalformedURLException("errormessage") }

        assertNull(updateChecker.getLatestVersionResponse())

        assertEquals(1, Notifications.unreadNotifications)
        assertEquals(
            "Failed to check for updates: invalid URL. Please inform the developer of this application. More detailed: errormessage.",
            Notifications.list.first().message
        )
    }

    @Test
    fun `test getRemoteTagResponse creates no notifications on another error and returns null`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).then { throw Exception("errormessage") }

        assertNull(updateChecker.getLatestVersionResponse())

        assertEquals(0, Notifications.unreadNotifications)
    }

    @Test
    fun `test getRemoteTags creates notification on JSON error and returns empty list`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn("invalid json message")

        val latestVersion = updateChecker.getLatestVersion()

        assertNull(latestVersion)
        assertEquals(1, Notifications.unreadNotifications)
        assertEquals(
            "Failed to check for updates: invalid JSON response. Please inform the developer of this application. More detailed: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path \$.",
            Notifications.list.first().message
        )
    }

    @Test
    fun `test getRemoteTags returns a list of tags from a JSON response`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn(
            """
            {
              "url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939",
              "assets_url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939/assets",
              "html_url": "https://github.com/sampie777/obs-scene-que/releases/tag/v2.7.0",
              "tag_name": "v2.7.0",
              "target_commitish": "master",
              "name": "v2.7.0",
              "draft": false,
              "prerelease": false,
              "created_at": "2020-10-14T07:52:16Z",
              "published_at": "2020-10-14T07:58:35Z"
            }
        """.trimIndent()
        )

        val version = updateChecker.getLatestVersion()

        assertEquals("v2.7.0", version)
    }

    @Test
    fun `test isNewUpdateAvailable returns true when latest version is different and unknown`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn(
            """
            {
              "url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939",
              "assets_url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939/assets",
              "html_url": "https://github.com/sampie777/obs-scene-que/releases/tag/v2.7.0",
              "tag_name": "v999.7.0",
              "target_commitish": "master",
              "name": "v999.7.0",
              "draft": false,
              "prerelease": false,
              "created_at": "2020-10-14T07:52:16Z",
              "published_at": "2020-10-14T07:58:35Z"
            }
        """.trimIndent()
        )
        updateChecker.updateLatestKnownVersion(ApplicationInfo.version)

        assertTrue(updateChecker.isNewUpdateAvailable())
        assertEquals("999.7.0", updateChecker.getLatestKnownVersion())
    }

    @Test
    fun `test isNewUpdateAvailable returns false when latest version is different but already known`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn(
            """
            {
              "url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939",
              "assets_url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939/assets",
              "html_url": "https://github.com/sampie777/obs-scene-que/releases/tag/v999.7.0",
              "tag_name": "v999.7.0",
              "target_commitish": "master",
              "name": "v999.7.0",
              "draft": false,
              "prerelease": false,
              "created_at": "2020-10-14T07:52:16Z",
              "published_at": "2020-10-14T07:58:35Z"
            }
        """.trimIndent()
        )
        updateChecker.updateLatestKnownVersion("999.7.0")

        assertFalse(updateChecker.isNewUpdateAvailable())
        assertEquals("999.7.0", updateChecker.getLatestKnownVersion())
    }

    @Test
    fun `test isNewUpdateAvailable returns false when latest version is not different but unknown`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn(
            """
            {
              "url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939",
              "assets_url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939/assets",
              "html_url": "https://github.com/sampie777/obs-scene-que/releases/tag/v${ApplicationInfo.version}",
              "tag_name": "v${ApplicationInfo.version}",
              "target_commitish": "master",
              "name": "v${ApplicationInfo.version}",
              "draft": false,
              "prerelease": false,
              "created_at": "2020-10-14T07:52:16Z",
              "published_at": "2020-10-14T07:58:35Z"
            }
        """.trimIndent()
        )
        updateChecker.updateLatestKnownVersion(ApplicationInfo.version)

        assertFalse(updateChecker.isNewUpdateAvailable())
        assertEquals(ApplicationInfo.version, updateChecker.getLatestKnownVersion())
    }

    @Test
    fun `test isNewUpdateAvailable returns false when latest version is not different and already known`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn(
            """
            {
              "url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939",
              "assets_url": "https://api.github.com/repos/sampie777/obs-scene-que/releases/32548939/assets",
              "html_url": "https://github.com/sampie777/obs-scene-que/releases/tag/v${ApplicationInfo.version}",
              "tag_name": "v${ApplicationInfo.version}",
              "target_commitish": "master",
              "name": "v${ApplicationInfo.version}",
              "draft": false,
              "prerelease": false,
              "created_at": "2020-10-14T07:52:16Z",
              "published_at": "2020-10-14T07:58:35Z"
            }
        """.trimIndent()
        )
        updateChecker.updateLatestKnownVersion(ApplicationInfo.version)

        assertFalse(updateChecker.isNewUpdateAvailable())
        assertEquals(ApplicationInfo.version, updateChecker.getLatestKnownVersion())
    }

    @Test
    fun `test isNewUpdateAvailable returns false when API returns invalid data`() {
        val urlMock = mock(wURL::class.java)
        val updateChecker = UpdateChecker(urlMock)
        `when`(urlMock.readText(anyString())).thenReturn("invalid json message")
        val latestKnownVersion = updateChecker.getLatestKnownVersion()

        assertFalse(updateChecker.isNewUpdateAvailable())
        assertEquals(1, Notifications.unreadNotifications)
        assertTrue(Notifications.list.first().message.contains("Failed to check for updates"))
        // Latest known version doesn't change
        assertEquals(latestKnownVersion, updateChecker.getLatestKnownVersion())
    }
}