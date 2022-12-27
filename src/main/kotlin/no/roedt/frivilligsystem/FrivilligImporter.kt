package no.roedt.frivilligsystem

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.ErMedlemStatus
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.person.UserId
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.Reader
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.annotation.PostConstruct
import javax.enterprise.context.Dependent

@Dependent
class FrivilligImporter(
    private val frivilligService: FrivilligService
) {

    lateinit var storage: Storage

    @PostConstruct
    fun getStorage() {
        storage = StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build().service
    }

    fun importer(userId: UserId, filnavn: String, bucketName: String) = CSVFormat.DEFAULT
        .builder()
        .setSkipHeaderRecord(true)
        .build()
        .parse(fil(filnavn, bucketName))
        .map { tilModellobjekt(it) }
        .map { AutentisertRegistrerNyFrivilligRequest(userId = userId, request = it) }
        .forEach { frivilligService.registrerNyFrivillig(it) }

    private fun fil(filnavn: String, bucketName: String): Reader {
        val bucket: Bucket = storage.get(bucketName)
        val blob: Blob = bucket.get(filnavn)
        val bytes = blob.getContent()
        val content = String(bytes)
        return StringReader(content)
    }

    private fun tilModellobjekt(it: CSVRecord): RegistrerNyFrivilligRequest {
        var i = 0
        val date = it.get(i++)
        val navn = it.get(i++)
        val epost = it.get(i++)
        val telefonnummer = it.get(i++)
        val postnummer = it.get(i++)
        val godtattVilkaar = it.get(i++)
        val alleredeAktivILokallag = it.get(i++)
        val medlemIRoedt = it.get(i++)

        // Aktiviteter
        val stand = it.get(i++)
        val some = it.get(i++)
        val doerbanking = it.get(i++)
        val ringing = it.get(i++)
        val sms = it.get(i++)
        val postkasseutdeling = it.get(i++)
        val morgenaksjon = it.get(i++)
        val bil = it.get(i++)

        // Fritekst
        val spesiellKompetanse = it.get(i++)
        val spraak = if (it.size() > 63) it.get(i++) else ""
        val spraak2 = if (it.size() > 63) it.get(i++) else ""
        val andreTingDuVilBidraMed = it.get(i++)
        val fortellLittOmDegSelv = it.get(i++)

        val politikkSjekkboksar = listOf<Pair<OpptattAv, String>>(
            Pair(OpptattAv.Oekedagpengenivaaet_spesieltforlavtloente, it.get(i++)),
            Pair(OpptattAv.Erstattemarkedsstyringmedtillitsreformioffentligsektor, it.get(i++)),
            Pair(OpptattAv.Kuttipolitikerloenningene, it.get(i++)),
            Pair(OpptattAv.Etmerrettferdigpensjonssystem, it.get(i++)),
            Pair(OpptattAv.Erstatthelseforetakenemedenaapenogdemokratiskstyringsmodell, it.get(i++)),
            Pair(OpptattAv.Gratisbarnehage, it.get(i++)),
            Pair(OpptattAv.Opprettelseavenlikeloennspottforaautjevneloennsforskjellerbasertpaakjoenn, it.get(i++)),
            Pair(OpptattAv.Oekebarnetrygdenogholdedenutenforberegningenavsosialhjelp, it.get(i++)),
            Pair(OpptattAv.Oekningiskattenefordesuperrike, it.get(i++)),
            Pair(OpptattAv.Forbyprivatebemanningsselskaper, it.get(i++)),
            Pair(OpptattAv.Sikreatingenmisterarbeidsavklaringspengerfoerdeeravklart, it.get(i++)),
            Pair(OpptattAv.Gratistannhelse, it.get(i++)),
            Pair(
                OpptattAv.Kutteiflateavgiftersomikketarhensyntilhvormyeduhar_hvorduborellerhvormyeduforbruker,
                it.get(i++)
            ),
            Pair(OpptattAv.MeldeNorgeutavNATOogjobbeforennordiskforsvarsallianse, it.get(i++)),
            Pair(OpptattAv.Innfoeringavdynastiskatt_enrettferdigogprogressivskattpaaluksusarv, it.get(i++)),
            Pair(OpptattAv.Sekstimersnormalarbeidsdag_30timersarbeidsuke, it.get(i++)),
            Pair(OpptattAv.Sikreuroertnatur, it.get(i++)),
            Pair(OpptattAv.Styrkekommuneoekonomien, it.get(i++)),
            Pair(OpptattAv.ErstatteEOeSavtalenmedenhandelsavtale, it.get(i++)),
            Pair(OpptattAv.Styrkebemanningenivelferden, it.get(i++)),
            Pair(OpptattAv.Postombaeringfemdageriuka, it.get(i++)),
            Pair(OpptattAv.Hindrediskrimineringograsismeiboligmarkedetogarbeidslivet, it.get(i++)),
            Pair(OpptattAv.Lovfestingavretttillaerlingplass, it.get(i++)),
            Pair(OpptattAv.Bedreogbilligerekollektivtransport, it.get(i++)),
            Pair(OpptattAv.Sikrenorskearbeidsplasserifiskeriet, it.get(i++)),
            Pair(OpptattAv.Atasylsoekereskalfaajobbemensdeventerpaasvar, it.get(i++)),
            Pair(OpptattAv.Stanseletingogutbyggingavnyeoljefelt, it.get(i++)),
            Pair(OpptattAv.Neitilnyevindkraftanleggpaaland, it.get(i++)),
            Pair(OpptattAv.Kutteiegenandelenpaahelsetjenester, it.get(i++)),
            Pair(OpptattAv.Styrkedetnorskeforsvaret, it.get(i++)),
            Pair(OpptattAv.Taimotflereasylsoekereogkvoteflyktninger, it.get(i++)),
            Pair(OpptattAv.Sikreatingenmisterdagpengeneutentilbudomjobbellerutdanning, it.get(i++)),
            Pair(OpptattAv.Enikkekommersiellboligsektormedbilligereboliger, it.get(i++)),
            Pair(OpptattAv.Tatilbakejernbanenioffentligregi, it.get(i++)),
            Pair(OpptattAv.Innfoeringavleksefriskole, it.get(i++)),
            Pair(OpptattAv.Profittfrivelferd, it.get(i++)),
            Pair(OpptattAv.SineitilEUdirektivsomsvekkerfagbevegelsenogrettigheteriarbeidslivet, it.get(i++)),
            Pair(
                OpptattAv.Brukedeleravoljefondetpaaaagjoerenoedvendigeinvesteringeriutbyggingavklimavennliginfrastruktur,
                it.get(i++)
            ),
            Pair(OpptattAv.Reverseringavtvangssammenslaattefylkerogkommuner, it.get(i++)),
            Pair(OpptattAv.Oekesosialstoenaden, it.get(i++))
//            Pair(OpptattAv.Oektnorskselvforsyningavmat_medisinerogannet, if (it.size() > 63) it.get(i++) else "false")
        )
            .filter { it.second.toBoolean() }
            .map { it.first }
            .map { it.name }

        // for dei nyaste er det nok ein ekstra sjekkboks her for mat og beredskap

        val haandtering = it.get(i++) // dette er fire radioboksar
        val personlig = it.get(i++) // ja/nei-radioboksar
        val tydelig = it.get(i++) // ja-nei-vet ikke-radioboksar
        val forslag = if (it.size() > 64) it.get(i) else "" // ja+fritekst eller nei

        val fornavn = navn.substringBeforeLast(" ")
        val etternavn = navn.substringAfterLast(" ")

        val aktiviteter = tilAktiviteter(stand, some, doerbanking, ringing, sms, postkasseutdeling, morgenaksjon, bil)

        return RegistrerNyFrivilligRequest(
            tidspunkt = LocalDateTime.parse(
                date.uppercase(),
                DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a", Locale.ENGLISH)
            ).toInstant(
                ZoneOffset.UTC
            ),
            fornavn = fornavn,
            etternavn = etternavn,
            epost = epost,
            telefonnummer = fixTelefonnummer(telefonnummer),
            postnummer = postnummer.toInt(),
            alleredeAktivILokallag = alleredeAktivILokallag == "Ja",
            spraak = spraak2,
            medlemIRoedt = ErMedlemStatus.valueOf(medlemIRoedt.replace(" ", "")),
            kanTenkeSegAaBidraMedAktiviteter = aktiviteter,
            spesiellKompetanse = spesiellKompetanse,
            andreTingDuVilBidraMed = andreTingDuVilBidraMed,
            fortellLittOmDegSelv = fortellLittOmDegSelv,
            opptattAv = politikkSjekkboksar,
            haandtering = haandtering,
            personlig = "Ja" == personlig,
            tydelig = tydelig,
            forslag = forslag
        )
    }

    private fun fixTelefonnummer(telefonnummer: String): String {
        val tlf = telefonnummer.replace("'", "").replace(" ", "").replace("0047", "+47")
        return if (tlf.length == 8) "+47$tlf" else tlf
    }

    private fun tilAktiviteter(
        stand: String?,
        some: String?,
        doerbanking: String?,
        ringing: String?,
        sms: String?,
        postkasseutdeling: String?,
        morgenaksjon: String?,
        bil: String?
    ): List<Aktivitet> = listOf(
        Pair(stand, Aktivitet.Stand),
        Pair(some, Aktivitet.SoMe),
        Pair(doerbanking, Aktivitet.Doerbanking),
        Pair(ringing, Aktivitet.Ringing),
        Pair(sms, Aktivitet.SMS),
        Pair(postkasseutdeling, Aktivitet.Postkasseutdeling),
        Pair(morgenaksjon, Aktivitet.Morgenaksjon),
        Pair(bil, Aktivitet.Bil)
    )
        .filter { it.first.toBoolean() }
        .map { it.second }
}
