package no.roedt.forum.database

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FirestoreCollections(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,

    @ConfigProperty(name = "projectId")
    private val projectId: String
) {
    var collections: List<FirestoreCollection>? = null

    @PostConstruct
    fun setup() {
        if (!brukHypersys) {
            collections = lagFakeCollections()
            return
        }
        val firestoreOptions = FirestoreOptions.getDefaultInstance()
            .toBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
        collections = firestoreOptions.service.listCollections()
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
