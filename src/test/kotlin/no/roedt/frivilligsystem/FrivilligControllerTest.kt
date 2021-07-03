package no.roedt.frivilligsystem

import com.nhaarman.mockitokotlin2.mock
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig
import org.junit.jupiter.api.Test

internal class FrivilligControllerTest {

    @Test
    fun henterFrivilligeMedGittEvne() {
        val service = FrivilligService(mock(), mock(), mock(), mock(), mock(), mock(), mock(), mock(), mock())
        val controller = FrivilligController(service, mock())

        val frivillig1 = Frivillig()
        val frivillig1SMS = AktivitetForFrivillig(frivillig_id = frivillig1.id, aktivitet = Aktivitet.SMS)

        val frivillig2 = Frivillig()
        val frivillig2Bil = AktivitetForFrivillig(frivillig_id = frivillig2.id, aktivitet = Aktivitet.Bil)

        val frivilligeSmoVilHaSMS = controller.hentAlleForAktivitet(mock(), Aktivitet.SMS)
    }
}