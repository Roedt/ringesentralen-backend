package no.roedt.ringesentralen.samtale.start

import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped

const val cacheTimeValidityInMillis: Long = 5000

@ApplicationScoped
class NyligeOppslagCache {
    private val hashMap = ConcurrentHashMap<Int, TimedEntry>()

    fun assertAtIngenAndreSoekerIDetteLagetAkkuratNo(lokallag: Int) {
        while (hashMap.containsKey(lokallag) && hashMap[lokallag]!!.isGyldig()) {
            Thread.sleep(100)
        }
        hashMap[lokallag] = TimedEntry()
    }

    fun remove(lokallag: Int) = hashMap.remove(lokallag)

    data class TimedEntry(val creationTime: Long = System.currentTimeMillis()) {
        fun isGyldig() = (System.currentTimeMillis() - creationTime) <= cacheTimeValidityInMillis
    }
}