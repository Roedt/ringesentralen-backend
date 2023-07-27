package no.roedt.frivilligsystem

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.DatabaseUpdater
import no.roedt.Emojifjerner
import no.roedt.Kilde
import no.roedt.brukere.FylkeRepository
import no.roedt.brukere.GenerellRolle
import no.roedt.frivilligsystem.kontakt.AutentisertRegistrerKontaktRequest
import no.roedt.frivilligsystem.kontakt.Kontakt
import no.roedt.frivilligsystem.kontakt.KontaktRepository
import no.roedt.frivilligsystem.kontakt.KontaktResponse
import no.roedt.frivilligsystem.registrer.Aktivitet
import no.roedt.frivilligsystem.registrer.AktivitetForFrivillig
import no.roedt.frivilligsystem.registrer.AktivitetForFrivilligRepository
import no.roedt.frivilligsystem.registrer.AutentisertRegistrerNyFrivilligRequest
import no.roedt.frivilligsystem.registrer.AutentisertSoMeFrivilligRequest
import no.roedt.frivilligsystem.registrer.ErMedlemStatus
import no.roedt.frivilligsystem.registrer.RegistrerNyFrivilligRequest
import no.roedt.lokallag.LokallagService
import no.roedt.person.Oppdateringskilde
import no.roedt.person.Person
import no.roedt.person.PersonDTO
import no.roedt.person.PersonService
import no.roedt.person.PostnummerRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import java.time.Instant

@ApplicationScoped
class FrivilligService(
    val frivilligRepository: FrivilligRepository,
    val personService: PersonService,
    val kontaktRepository: KontaktRepository,
    val databaseUpdater: DatabaseUpdater,
    val lokallagService: LokallagService,
    val fylkeRepository: FylkeRepository,
    val aktivitetForFrivilligRepository: AktivitetForFrivilligRepository,
    val frivilligOpptattAvRepository: FrivilligOpptattAvRepository,
    val frivilligKoronaRepository: FrivilligKoronaRepository,
    val postnummerRepository: PostnummerRepository
) {
    fun hentAlle(userId: UserId, roller: Set<String>) =
        hentFrivilligeUtFraMinRolle(roller, personService.getPerson(userId))
            .map { Pair(it, personService.findById(it.personId)) }
            .map {
                FrivilligResponse(
                    frivillig = it.first,
                    person = PersonDTO.fra(it.second),
                    aktiviteter = aktivitetForFrivilligRepository.list("frivillig_id", it.first.id),
                    fylke = fylkeRepository.findById(it.second.fylke),
                    lokallag = lokallagService.findById(it.second.lokallag),
                    kontakt = kontaktRepository.list("frivillig_id", it.first.id.toInt()).map { i ->
                        KontaktResponse(
                            frivillig_id = i.frivillig_id,
                            tilbakemelding = i.tilbakemelding,
                            registrert_av = personService.findById(i.registrert_av),
                            datetime = i.datetime
                        )
                    },
                    opptattAv = frivilligOpptattAvRepository.list("frivillig_id", it.first.id).map { i -> i.opptattAv }
                        .map { i -> i.displaytext },
                    frivilligKorona = frivilligKoronaRepository.find("frivillig_id", it.first.id)
                        .firstResultOptional<FrivilligKorona>().orElse(null)
                )
            }

    private fun hentFrivilligeUtFraMinRolle(roller: Set<String>, ringer: Person): List<Frivillig> = when {
        roller.contains(GenerellRolle.admin) -> frivilligRepository.listAll()
        roller.contains(RingespesifikkRolle.godkjenner) -> {
            databaseUpdater.getResultList("select f.id from frivillig f inner join person p on p.id = f.personId and p.fylke=${ringer.fylke} ORDER BY p.etternavn ASC")
                .map { it as Int }
                .map { frivilligRepository.findById(it) }
        }

        else -> databaseUpdater.getResultList("select f.id from frivillig f inner join person p on p.id = f.personId and p.lokallag=${ringer.lokallag} ORDER BY p.etternavn ASC")
            .map { it as Int }
            .map { frivilligRepository.findById(it) }
    }

    fun registrerNyFrivillig(autentisertRequest: AutentisertRegistrerNyFrivilligRequest): Pair<Boolean, Frivillig> {
        val request = autentisertRequest.request
        val person = request.toPerson()
        val id = personService.save(person, Oppdateringskilde.RegistrertFrivillig)
        val personId = person.id?.toInt() ?: id.toInt()
        if (frivilligRepository.count("personId", personId) > 0L) {
            return Pair(false, frivilligRepository.find("personId", personId).firstResult())
        }
        val frivillig = Frivillig(
            personId = personId,
            alleredeAktivILokallag = request.alleredeAktivILokallag,
            medlemIRoedt = request.medlemIRoedt,
            spesiellKompetanse = Emojifjerner.fjernEmojis(request.spesiellKompetanse),
            andreTingDuVilBidraMed = Emojifjerner.fjernEmojis(request.andreTingDuVilBidraMed),
            spraak = Emojifjerner.fjernEmojis(request.spraak),
            fortellLittOmDegSelv = Emojifjerner.fjernEmojis(request.fortellLittOmDegSelv),
            registrertTidspunkt = request.tidspunkt ?: Instant.now()
        )
        frivilligRepository.persist(frivillig)
        request.kanTenkeSegAaBidraMedAktiviteter
            .map { AktivitetForFrivillig(frivillig_id = frivillig.id.toInt(), aktivitet = it) }
            .forEach { aktivitetForFrivilligRepository.persist(it) }
        lagreOpptattAv(request, frivillig)

        frivilligKoronaRepository.persist(
            FrivilligKorona(
                frivillig_id = frivillig.id,
                haandtering = Emojifjerner.fjernEmojis(request.haandtering) ?: "",
                personlig = request.personlig,
                tydelig = Emojifjerner.fjernEmojis(request.tydelig) ?: "",
                forslag = Emojifjerner.fjernEmojis(request.forslag) ?: ""
            )
        )

        return Pair(true, frivillig)
    }

    private fun lagreOpptattAv(
        request: RegistrerNyFrivilligRequest,
        frivillig: Frivillig
    ) {
        try {
            request.opptattAv
                .map { OpptattAv.valueOf(it) }
                .map { FrivilligOpptattAv(frivillig_id = frivillig.id, opptattAv = it) }
                .forEach { frivilligOpptattAvRepository.persist(it) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun RegistrerNyFrivilligRequest.toPerson(): Person {
        val postnr = postnummerRepository.findById(postnummer)
        val lokallag = lokallagService.fromPostnummer(postnr)
        val person = Person(
            hypersysID = null,
            fornavn = fornavn,
            etternavn = etternavn,
            telefonnummer = telefonnummer,
            email = epost,
            postnummer = postnr,
            fylke = fylkeRepository.getFylke(lokallag, postnr),
            lokallag = lokallag,
            groupID = RingesentralenGroupID.Frivillig.nr,
            kilde = Kilde.Frivillig,
            sistOppdatert = null
        )
        val eksisterendePerson = personService.finnPerson(person = person)
        if (!RingesentralenGroupID.isBrukerEllerVenter(eksisterendePerson?.groupID() ?: -1)) {
            eksisterendePerson?.setGroupID(RingesentralenGroupID.Frivillig)
        }
        return eksisterendePerson ?: person
    }

    fun registrerKontakt(request: AutentisertRegistrerKontaktRequest) =
        kontaktRepository.persist(
            Kontakt(
                frivillig_id = request.request.frivillig_id,
                tilbakemelding = request.request.tilbakemelding,
                registrert_av = personService.finnFraHypersysId(request.userId.userId)
                    .firstResult<Person>().id.toInt(),
                datetime = Instant.now()
            )
        )

    fun hentAlleForAktivitet(userId: UserId, groups: Set<String>, aktivitet: Aktivitet) = hentAlle(userId, groups)
        .filter { frivillig -> frivillig.aktiviteter.map { it.aktivitet }.contains(aktivitet) }

    fun registrerNySoMeFrivillig(autentisertSoMeFrivilligRequest: AutentisertSoMeFrivilligRequest): Pair<Boolean, Frivillig> =
        registrerNyFrivillig(
            AutentisertRegistrerNyFrivilligRequest(
                userId = autentisertSoMeFrivilligRequest.userId,
                request = RegistrerNyFrivilligRequest(
                    tidspunkt = Instant.now(),
                    fornavn = autentisertSoMeFrivilligRequest.request.fornavn,
                    etternavn = autentisertSoMeFrivilligRequest.request.etternavn,
                    telefonnummer = autentisertSoMeFrivilligRequest.request.telefonnummer,
                    postnummer = autentisertSoMeFrivilligRequest.request.postnummer,
                    alleredeAktivILokallag = false,
                    andreTingDuVilBidraMed = "Meldte seg direkte som SoMe-frivillig",
                    epost = autentisertSoMeFrivilligRequest.request.email ?: "",
                    forslag = "",
                    fortellLittOmDegSelv = "Meldte seg direkte som SoMe-frivillig",
                    haandtering = "",
                    kanTenkeSegAaBidraMedAktiviteter = listOf(Aktivitet.SoMe),
                    medlemIRoedt = ErMedlemStatus.Ukjent,
                    opptattAv = listOf(),
                    personlig = false,
                    spesiellKompetanse = "",
                    spraak = "",
                    tydelig = ""
                )
            )
        )
}
