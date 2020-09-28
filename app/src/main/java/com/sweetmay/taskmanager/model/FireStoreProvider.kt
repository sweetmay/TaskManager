package com.sweetmay.taskmanager.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sweetmay.taskmanager.model.errors.NoAuthException
import java.lang.IllegalArgumentException

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

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        return try {
            notesReference.addSnapshotListener{snapshot, e ->
                e?.let {
                    result.value = NoteResult.Error(it)
                }?: snapshot?.let {
                    val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                    result.value = NoteResult.Success(notes)
                }
            }

            result
        }catch (e: NoAuthException){
            result.value = NoteResult.Error(e)
            result
        }

    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        return try {
            notesReference.document(note.id).set(note).addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener{
                result.value = NoteResult.Error(it)
            }
            result
        }catch (e: NoAuthException){
            result.value = NoteResult.Error(e)
            result
        }
    }

    override fun getById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        return try {
            notesReference.document(id).get().addOnSuccessListener {
                val note = it.toObject(Note::class.java)
                result.value = NoteResult.Success(note)
            }.addOnFailureListener{
                result.value = NoteResult.Error(it)
            }
            result
        }catch (e: IllegalArgumentException){
            result.value = NoteResult.Error(e)
            result
        }
    }

    override fun deleteNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        return try {
            notesReference.document(id).delete().addOnSuccessListener {
                result.value = NoteResult.Success(it)
            }.addOnFailureListener{
                result.value = NoteResult.Error(it)
            }
            result
        }catch (e: IllegalArgumentException){
            result.value = NoteResult.Error(e)
            result
        }
    }
}