package no.roedt.ringesentralen.hypersys

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AESUtilUnitTest {
    @Test
    fun givenString_whenEncrypt_thenSuccess() {
        val kryptertFraFrontend = "bdb191129ea6082a6adecd62f780f943:72b5cdf548bef46534103c45f15a3198335ae5b6fc34583a080a0cb0916fc8b5"

        val input = "dette er en veldig fin tekst"

        val aesUtil = AESUtil()
        aesUtil.setKey()

        val ivParameterSpec = aesUtil.generateIv()

        val cipherText = aesUtil.encrypt(input, ivParameterSpec)
        val plainText = aesUtil.decrypt(cipherText,ivParameterSpec)

//        aesUtil.decrypt(kryptertFraFrontend, iv = null)

        Assertions.assertEquals(input, plainText)
    }
}