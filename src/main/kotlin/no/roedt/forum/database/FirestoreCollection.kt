package no.roedt.forum.database

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference

interface FirestoreCollection {
    val underforumnavn: String
    fun listDocuments(): Iterable<FirestoreDocument>
    fun document(tittel: String): FirestoreDocument
}

data class RealFirestoreCollection(override val underforumnavn: String, val collectionReference: CollectionReference) :
    FirestoreCollection {
    override fun listDocuments(): Iterable<FirestoreDocument> = collectionReference.listDocuments()
        .map { RealFirestoreDocument(tittel = it.id, underforum = it.parent.path, firestoreReferanse = it) }

    override fun document(tittel: String) = collectionReference.document(tittel).tilFirestoreDocument()
}

private fun DocumentReference.tilFirestoreDocument(): FirestoreDocument = RealFirestoreDocument(
    tittel = this.id,
    underforum = this.parent.path,
    firestoreReferanse = this
)

class FakeFirestoreCollection(override val underforumnavn: String, private val dokumenter: Iterable<FirestoreDocument>) : FirestoreCollection {
    override fun listDocuments(): Iterable<FirestoreDocument> = dokumenter
    override fun document(tittel: String): FirestoreDocument = dokumenter.first { it.tittel == tittel }
}
