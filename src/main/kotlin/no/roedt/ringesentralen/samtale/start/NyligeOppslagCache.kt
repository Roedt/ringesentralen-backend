package no.roedt.ringesentralen.samtale.start

import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NyligeOppslagCache {
    private var cacheTimeValidityInMillis: Long = 5000
    private val hashMap = ConcurrentHashMap<Int, TimedEntry>()

    fun assertAtIngenAndreSoekerIDetteLagetAkkuratNo(lokallag: Int) {
        var i = 0
        while (hashMap.containsKey(lokallag) && hashMap[lokallag]!!.isGyldig()) {
            Thread.sleep(100);
            i++
        }
        hashMap[lokallag] = TimedEntry(cacheTimeValidityInMillis)
    }

    fun remove(lokallag: Int) = hashMap.remove(lokallag)

    data class TimedEntry(val maxDurationInMillis: Long) {
        private val creationTime: Long = System.currentTimeMillis()
        fun isGyldig() = (System.currentTimeMillis() - creationTime) <= maxDurationInMillis
    }
}