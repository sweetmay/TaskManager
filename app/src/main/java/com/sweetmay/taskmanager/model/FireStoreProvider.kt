package com.sweetmay.taskmanager.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.sweetmay.taskmanager.model.errors.NoAuthException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(val firebaseAuth: FirebaseAuth, val store: FirebaseFirestore) : DataProvider {

    companion object{
        private const val NOTES_COLLECTION_KEY = "notes"
        private const val USERS_COLLECTION_KEY = "users"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    private val notesReference
        get() =  currentUser?.let { store.collection(USERS_COLLECTION_KEY).document(it.uid).collection(
        NOTES_COLLECTION_KEY) } ?: throw NoAuthException()

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        })
    }

    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> = Channel<NoteResult>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null
        try {
            registration = notesReference.addSnapshotListener{snapshot, e ->
                val value =  e?.let {
                    NoteResult.Error(it)
                }?: snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    NoteResult.Success(notes)
                }

                value?.let { offer(it) }
            }
        }catch (e: NoAuthException){
            offer(NoteResult.Error(e))
        }
        invokeOnClose { registration?.remove() }
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    continuation.resume(note)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (t: Throwable) {
            continuation.resumeWithException(t)
        }
    }

    override suspend fun getById(id: String): Note? = suspendCoroutine {continuation ->
        try {
            notesReference.document(id).get()
                .addOnSuccessListener { snapshot ->
                    val note = snapshot.toObject(Note::class.java)
                    continuation.resume(note)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (t: Throwable) {
            continuation.resumeWithException(t)
        }
    }

    override suspend fun deleteNoteById(id: String): Unit = suspendCoroutine{continuation ->

        try {
            notesReference.document(id).delete()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (t: Throwable) {
            continuation.resumeWithException(t)
        }
    }
}