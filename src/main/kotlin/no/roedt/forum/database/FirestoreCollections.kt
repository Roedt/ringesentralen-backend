package no.roedt.forum.database

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FirestoreCollections(
    private val firestoreCollectionFactory: FirestoreCollectionFactory
) {
    var collections: List<FirestoreCollection>? = null

    @PostConstruct
    fun setup() {
        collections = firestoreCollectionFactory.listCollections()
    }
}
