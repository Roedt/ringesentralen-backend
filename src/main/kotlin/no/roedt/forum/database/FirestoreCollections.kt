package no.roedt.forum.database

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FirestoreCollections(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,
    firestore: Firestore
) {
    var collections = if (!brukHypersys) {
        lagFakeCollections()
    } else {
        listCollections(firestore)
            .map { RealFirestoreCollection(underforumnavn = it.id, collectionReference = it) }
            .toList()
    }

    private fun listCollections(firestore: Firestore): MutableIterable<CollectionReference> {
        val listCollections = firestore.listCollections()
        println(listCollections.count())
        listCollections.map { it.id }.forEach { println(it) }
        return listCollections
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
