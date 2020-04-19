package objects

import java.util.*
import java.util.logging.Logger


class TScene {
    private val logger = Logger.getLogger(TScene::class.java.name)
    var name = ""
    var sources: List<TSource> = ArrayList()

    fun maxVideoLength(): Int {
        val longestVideoLengthSource = longestVideoLengthSource()
        if (!longestVideoLengthSource.isPresent) {
            logger.info("No longest video source found for TScene $name")
        } else {
            logger.info("Longest video source for TScene '" + name + "' has length = " + longestVideoLengthSource.get().videoLength)
        }
        return longestVideoLengthSource.map(TSource::videoLength).orElse(0)
    }

    private fun longestVideoLengthSource(): Optional<TSource> =
        sources.stream().max(Comparator.comparingInt(TSource::videoLength))

    override fun toString(): String {
        return name
    }
}
