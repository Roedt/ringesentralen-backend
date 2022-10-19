package no.roedt.forum.database

import com.google.cloud.firestore.DocumentReference

interface FirestoreDocument {
    val tittel: String
    val underforum: String
    var innhold: MutableMap<String, Any>?
}

class RealFirestoreDocument(
    override val tittel: String,
    override val underforum: String,
    private val firestoreReferanse: DocumentReference
) : FirestoreDocument {
    override var innhold: MutableMap<String, Any>?
        get() =
            firestoreReferanse.get().get().data
        set(value) {
            if (value != null) {
                firestoreReferanse.set(value)
            }
        }
}

class FakeFirestoreDocument(override val tittel: String, override val underforum: String) : FirestoreDocument {
    override var innhold: MutableMap<String, Any>? = mutableMapOf()
}
