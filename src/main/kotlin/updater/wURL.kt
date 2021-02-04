package updater

import java.net.URL
import java.nio.charset.Charset

/**
 * Wrapper for java.net.URL in order to make it mockable
 */
open class wURL {
    // This somewhat redundant class is necessary for mockito tests
    open fun readText(url: String): String {
        return readText(url, Charsets.UTF_8)
    }

    open fun readText(url: String, charset: Charset): String {
        return URL(url).readText(charset)
    }
}