package no.roedt.forum.database

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

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

    @PostConstruct
    fun setup() {
        if (!brukHypersys) {
            collections = lagFakeCollections()
            return
        }
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setProjectId(projectId)
            .setDatabaseUrl("https://ringesentralen-produksjon.firebaseio.com/")
            .build()
            .let { if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(it) }
        val db: Firestore = FirestoreClient.getFirestore()

        val fs = if (!usePrivateKeyFromSecretManager) localFirestoreconnection() else db.listCollections()
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
