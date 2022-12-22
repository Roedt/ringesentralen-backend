package no.roedt.forum.database

import com.google.cloud.firestore.Firestore
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.Produces

interface FirestoreCollectionFactory {
    fun listCollections(): List<FirestoreCollection>?
}

class RealFirestoreCollectionFactory : FirestoreCollectionFactory {
    @Inject
    private lateinit var firestore: Firestore

    override fun listCollections() = firestore.listCollections()
        .map { RealFirestoreCollection(underforumnavn = it.id, collectionReference = it) }
}

class FakeFirestoreCollectionFactory : FirestoreCollectionFactory {
    override fun listCollections() = listOf(
        FakeFirestoreCollection(
            underforumnavn = "Om forumet",
            dokumenter = listOf(
                FakeFirestoreDocument("Godt laga!", "Om forumet").also {
                    it.innhold = mutableMapOf(Pair("innhold", "Eg synes forumet er bra laga."))
                },
                FakeFirestoreDocument("Godt laga2!", "Om forumet").also {
                    it.innhold = mutableMapOf(Pair("innhold", "Einig!"))
                }
            )
        ),
        FakeFirestoreCollection(
            underforumnavn = "Klima",
            dokumenter = listOf()
        )
    )
}

@ApplicationScoped
class FirestoreProducer(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean
) {

    @Produces
    @RequestScoped
    fun lagFirestoreCollections() = if (brukHypersys) {
        RealFirestoreCollectionFactory()
    } else {
        FakeFirestoreCollectionFactory()
    }
}
