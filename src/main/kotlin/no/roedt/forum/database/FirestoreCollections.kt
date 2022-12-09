package no.roedt.forum.database

import com.google.cloud.firestore.Firestore
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class FirestoreCollections(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean
) {
    var collections: List<FirestoreCollection>? = null

    @Inject
    private lateinit var firestore: Firestore

    @PostConstruct
    fun setup() {
        if (!brukHypersys) {
            collections = lagFakeCollections()
            return
        }
        collections = firestore.listCollections()
            .map { RealFirestoreCollection(underforumnavn = it.id, collectionReference = it) }
            .toList()
    }

    private fun lagFakeCollections() = listOf(
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
