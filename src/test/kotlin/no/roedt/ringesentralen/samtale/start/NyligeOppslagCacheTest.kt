package no.roedt.ringesentralen.samtale.start

import org.junit.jupiter.api.Test
import java.util.stream.IntStream
import kotlin.concurrent.thread

internal class NyligeOppslagCacheTest {

    @Test
    fun run() {
        val cache = NyligeOppslagCache()
        IntStream.range(0, 1000).forEach {
            thread(start = true) {
                cache.assertAtIngenAndreSoekerIDetteLagetAkkuratNo(1)
            }
        }
    }
}