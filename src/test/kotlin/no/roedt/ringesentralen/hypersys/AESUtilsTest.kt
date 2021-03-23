package no.roedt.ringesentralen.hypersys

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.ringesentralen.token.SecretFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AESUtilUnitTest {

    private val secretFactory: SecretFactory = mock()

    @Test
    fun givenString_whenEncrypt_thenSuccess() {
        val kryptertFraFrontend = "bdb191129ea6082a6adecd62f780f943:72b5cdf548bef46534103c45f15a3198335ae5b6fc34583a080a0cb0916fc8b5"
        val keyString = "Ek6MKdSdEz1EiRs6cX3uUUAAnMYua17h"
        doReturn(keyString).whenever(secretFactory).getEncryptionKey()

        val input = "dette er en veldig fin tekst"

        val aesUtil = AESUtil(secretFactory)
        aesUtil.setKey()

        val ivParameterSpec = aesUtil.generateIv()

        val cipherText = aesUtil.encrypt(input, ivParameterSpec)
        val plainText = aesUtil.decrypt(cipherText,ivParameterSpec)

//        aesUtil.decrypt(kryptertFraFrontend, iv = null)

        Assertions.assertEquals(input, plainText)
    }
}