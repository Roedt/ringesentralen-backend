package no.roedt.forum.database

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.FirestoreOptions
import no.roedt.forum.underforum.Traad
import no.roedt.forum.underforum.Underforum
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ForumRepository {

    private var collections: List<CollectionReference>? = null

    @Inject
    @ConfigProperty(name = "projectId")
    var projectId: String? = null

    @PostConstruct
    fun setup() {
        val firestoreOptions = FirestoreOptions.getDefaultInstance()
            .toBuilder()
            .setProjectId(projectId)
            .build()
        collections = firestoreOptions.service.listCollections().toList()
    }

    fun hentAlleUnderforum(): List<Underforum> = collections?.map { it.tilUnderforum() } ?: listOf()

    fun hentTraader(underforum: Underforum): List<Traad> =
        collections?.find { it.id == underforum.id }?.listDocuments()?.map { it.tilTraad() } ?: listOf()
}

fun CollectionReference.tilUnderforum() = Underforum(
    id = this.id
)

fun DocumentReference.tilTraad() = Traad(
    tittel = this.id
)
