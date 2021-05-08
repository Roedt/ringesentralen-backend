package no.roedt.frivilligsystem

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import io.quarkiverse.googlecloudservices.common.GcpConfiguration
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.ErMedlemStatus
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.ringesentralen.person.UserId
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.Reader
import java.io.StringReader
import javax.annotation.PostConstruct
import javax.enterprise.context.Dependent


@Dependent
class FrivilligImporter(
    private val frivilligService: FrivilligService,
    private val gcpConfiguration: GcpConfiguration
) {

    lateinit var storage: Storage

    @PostConstruct
    fun getStorage() {
        storage = StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setProjectId(gcpConfiguration.projectId)
            .build().service
    }

    fun importer(userId: UserId, filnavn: String, bucketName: String) = CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
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
        val spesiellKompetanse = if (it.size() > 63) it.get(i++) else null
        val spraak = if (it.size() > 63) it.get(i++) else null
        val andreTingDuVilBidraMed = if (it.size() > 63) it.get(i++) else null
        val fortellLittOmDegSelv = if (it.size() > 63) it.get(i++) else null

        // Politikk-sjekkboksar
        val oekedagpengenivaaet = it.get(i++)
        val spesieltforlavtloente = it.get(i++)
        val Erstattemarkedsstyringmedtillitsreformioffentligsektor = it.get(i++)
        val Kuttipolitikerloenningene = it.get(i++)
        val Etmerrettferdigpensjonssystem = it.get(i++)
        val Erstatthelseforetakenemedenaapenogdemokratiskstyringsmodell = it.get(i++)
        val Gratisbarnehage = it.get(i++)
        val Opprettelseavenlikeloennspottforaautjevneloennsforskjellerbasertpaakjoenn = it.get(i++)
        val oekebarnetrygdenogholdedenutenforberegningenavsosialhjelp = it.get(i++)
        val oekningiskattenefordesuperrike = it.get(i++)
        val Forbyprivatebemanningsselskaper = it.get(i++)
        val Sikreatingenmisterarbeidsavklaringspengerfoerdeeravklart = it.get(i++)
        val Gratistannhelse = it.get(i++)
        val Kutteiflateavgiftersomikketarhensyntilhvormyeduhar = it.get(i++)
        val hvorduborellerhvormyeduforbruker = it.get(i++)
        val MeldeNorgeutavNATOogjobbeforennordiskforsvarsallianse = it.get(i++)
        val Innfoeringavdynastiskatt = it.get(i++)
        val enrettferdigogprogressivskattpaaluksusarv = it.get(i++)
        val Sekstimersnormalarbeidsdag_30timersarbeidsuke = it.get(i++)
        val Sikreuroertnatur = it.get(i++)
        val Styrkekommuneoekonomien = it.get(i++)
        val ErstatteEoeSavtalenmedenhandelsavtale = it.get(i++)
        val Styrkebemanningenivelferden = it.get(i++)
        val Postombaeringfemdageriuka = it.get(i++)
        val Hindrediskrimineringograsismeiboligmarkedetogarbeidslivet = it.get(i++)
        val Lovfestingavretttillaerlingplass = it.get(i++)
        val Bedreogbilligerekollektivtransport = it.get(i++)
        val Sikrenorskearbeidsplasserifiskeriet = it.get(i++)
        val Atasylsoekereskalfaajobbemensdeventerpaasvar = it.get(i++)
        val Stanseletingogutbyggingavnyeoljefelt = it.get(i++)
        val Neitilnyevindkraftanleggpaaland = it.get(i++)
        val Kutteiegenandelenpaahelsetjenester = it.get(i++)
        val Styrkedetnorskeforsvaret = it.get(i++)
        val Taimotflereasylsoekereogkvoteflyktninger = it.get(i++)
        val Sikreatingenmisterdagpengeneutentilbudomjobbellerutdanning = it.get(i++)
        val Enikkekommersiellboligsektormedbilligereboliger = it.get(i++)
        val Tatilbakejernbanenioffentligregi = it.get(i++)
        val Innfoeringavleksefriskole = it.get(i++)
        val Profittfrivelferd = it.get(i++)
        val SineitilEUdirektivsomsvekkerfagbevegelsenogrettigheteriarbeidslivet = it.get(i++)
        val Brukedeleravoljefondetpaaaagjoerenoedvendigeinvesteringeriutbyggingavklimavennliginfrastruktur = it.get(i++)
        val Reverseringavtvangssammenslaattefylkerogkommuner = it.get(i++)
        val oekesosialstoenaden = it.get(i++)
        val Oektnorskselvforsyningavmat_medisinerogannet = if (it.size() > 63) it.get(i++) else null

        // for dei nyaste er det nok ein ekstra sjekkboks her for mat og beredskap

        val haandtering = it.get(i++) // dette er fire radioboksar
        val personlig = it.get(i++) // ja/nei-radioboksar
        val tydelig = it.get(i++) // ja-nei-vet ikke-radioboksar
        val forslag = it.get(i) // ja+fritekst eller nei

        val fornavn = navn.substringBeforeLast(" ")
        val etternavn = navn.substringAfterLast(" ")

        val aktiviteter = tilAktiviteter(stand, some, doerbanking, ringing, sms, postkasseutdeling, morgenaksjon, bil)

        return RegistrerNyFrivilligRequest(
            fornavn = fornavn,
            etternavn = etternavn,
            epost = epost,
            telefonnummer = telefonnummer,
            postnummer = postnummer.toInt(),
            alleredeAktivILokallag = alleredeAktivILokallag == "Ja",
            medlemIRoedt = ErMedlemStatus.valueOf(medlemIRoedt.replace(" ", "")),
            kanTenkeSegAaBidraMedAktiviteter = aktiviteter,
            spesiellKompetanse = spesiellKompetanse,
            andreTingDuVilBidraMed = andreTingDuVilBidraMed,
            fortellLittOmDegSelv = fortellLittOmDegSelv
        )
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