package no.roedt.forum.database

import com.google.cloud.firestore.Firestore
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

interface FirestoreCollectionFactory {
    fun listCollections(): List<FirestoreCollection>?
}

class RealFirestoreCollectionFactory(private val firestore: Firestore) : FirestoreCollectionFactory {
    override fun listCollections() =
        firestore.listCollections()
            .map { RealFirestoreCollection(underforumnavn = it.id, collectionReference = it) }
}

class FakeFirestoreCollectionFactory : FirestoreCollectionFactory {
    override fun listCollections() =
        listOf(
            FakeFirestoreCollection(
                underforumnavn = "Om forumet",
                dokumenter =
                    listOf(
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
    private val brukHypersys: Boolean,
    private val firestore: Firestore
) {
    @Produces
    @RequestScoped
    fun lagFirestoreCollections() =
        if (brukHypersys) {
            RealFirestoreCollectionFactory(firestore)
        } else {
            FakeFirestoreCollectionFactory()
        }
}
