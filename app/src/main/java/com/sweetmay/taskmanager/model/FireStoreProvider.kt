package com.sweetmay.taskmanager.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreProvider : DataProvider {

    companion object{
        private const val NOTES_COLLECTION_KEY = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection((NOTES_COLLECTION_KEY))

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener{snapshot, e ->
            e?.let {
                result.value = NoteResult.Error(it)
            }?: snapshot?.let {
                val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                result.value = NoteResult.Success(notes)
            }
        }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note).addOnSuccessListener {
            result.value = NoteResult.Success(note)
        }.addOnFailureListener{
            result.value = NoteResult.Error(it)
        }
        return result
    }

    override fun getById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get().addOnSuccessListener {
            val note = it.toObject(Note::class.java)
                result.value = NoteResult.Success(note)
        }.addOnFailureListener{
            result.value = NoteResult.Error(it)
        }
        return result
    }
}