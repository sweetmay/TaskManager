package com.sweetmay.taskmanager.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.sweetmay.taskmanager.model.FireStoreProvider
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.errors.NoAuthException
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreProviderTest {

    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockCollectionReference = mockk<CollectionReference>()

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    @Before
    fun setup(){
        clearAllMocks()
        every { mockUser.uid } returns ""
        every { mockAuth.currentUser } returns mockUser
        every { mockDb.collection(any()).document(mockUser.uid).collection(any()) } returns mockCollectionReference

        every { mockDocument1.toObject((Note::class.java)) } returns testNotes[0]
        every { mockDocument2.toObject((Note::class.java)) } returns testNotes[1]
        every { mockDocument3.toObject((Note::class.java)) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if not auth`(){
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever{
            result = (it as NoteResult.Error).error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `saveNote calls set`(){
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        every { mockCollectionReference.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns success note`(){
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockCollectionReference.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()
        provider.saveNote(testNotes[0]).observeForever{
            result = (it as? NoteResult.Success<Note>)?.data
        }

        slot.captured.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `subscribeToAllNotes returns notes`(){
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollectionReference.addSnapshotListener(capture(slot)) } returns mockk()
        provider.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`(){
        var result: Throwable? = null
        val mockError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockCollectionReference.addSnapshotListener(capture(slot)) } returns mockk()
        provider.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Error)?.error
        }
        slot.captured.onEvent(null, mockError)
        assertEquals(mockError, result)
    }

    @Test
    fun `getById returns note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()
        every { mockCollectionReference.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.get().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.getById(testNotes[0].id).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }

        slot.captured.onSuccess(mockDocument1)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `deleteNoteById deletes note`(){
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockCollectionReference.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNoteById(testNotes[0].id).observeForever{
            result = (it as? NoteResult.Success<*>)?.data
        }
        slot.captured.onSuccess(null)
        assertEquals(result, null)
    }
}