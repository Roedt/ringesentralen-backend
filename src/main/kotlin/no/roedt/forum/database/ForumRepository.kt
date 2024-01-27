package no.roedt.forum.database

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.TraadMedInnhold
import no.roedt.forum.underforum.TraadRequest
import no.roedt.forum.underforum.Underforum

@ApplicationScoped
class ForumRepository(val firestoreCollections: FirestoreCollections) {
    fun hentAlleUnderforum(): List<Underforum> = firestoreCollections.collections?.map { it.tilUnderforum() } ?: listOf()

    fun hentTraader(underforum: String): List<Traad> = finnUnderforum(underforum)?.listDocuments()?.map { it.tilTraad() } ?: listOf()

    fun hentTraad(traadId: Traad): TraadMedInnhold = finnUnderforum(traadId.underforum)!!.document(traadId.tittel).tilTraadMedInnhold()

    private fun finnUnderforum(id: String) = firestoreCollections.collections?.find { it.underforumnavn == id }

    fun opprettTraad(traad: TraadRequest) {
        finnUnderforum(traad.underforum)!!.document(traad.id).innhold = mutableMapOf(Pair("innhold", traad.node))
    }
}

fun FirestoreCollection.tilUnderforum() =
    Underforum(
        id = this.underforumnavn
    )

fun FirestoreDocument.tilTraad() =
    Traad(
        tittel = this.tittel,
        underforum = this.underforum
    )

private fun FirestoreDocument.tilTraadMedInnhold(): TraadMedInnhold = TraadMedInnhold(traad = this.tilTraad(), innhold = this.innhold)
