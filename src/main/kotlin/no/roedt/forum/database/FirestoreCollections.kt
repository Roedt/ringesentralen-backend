package no.roedt.forum.database

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class FirestoreCollections(
    @ConfigProperty(name = "brukHypersys", defaultValue = "true")
    private val brukHypersys: Boolean,

    @ConfigProperty(name = "projectId")
    private val projectId: String,

    @ConfigProperty(name = "usePrivateKeyFromSecretManager")
    private val usePrivateKeyFromSecretManager: Boolean
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
        val fs = localFirestoreconnection()
        collections = fs
            .map { RealFirestoreCollection(underforumnavn = it.id, collectionReference = it) }
            .toList()
    }

    private fun localFirestoreconnection(): MutableIterable<CollectionReference> {
        val applicationDefault = GoogleCredentials.getApplicationDefault()
        print(applicationDefault)
        return FirestoreOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(applicationDefault)
            .build()
            .service
            .listCollections()
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
