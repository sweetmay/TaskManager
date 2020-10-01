package com.sweetmay.taskmanager.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.App
import com.sweetmay.taskmanager.view.ui.NoteViewState
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>(relaxed = true)
    private val noteLiveData = MutableLiveData<NoteResult>()
    private val testData = Note(id = "1")
    private val testTitle = "title"
    private val testBody = "body"
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        clearAllMocks()
        viewModel = NoteViewModel(mockRepository)
        every { mockRepository.getNoteById(testData.id) } returns noteLiveData
    }

    @Test
    fun `should load note by id`(){
        var result: Note? = null
        viewModel.getViewState().observeForever{
            result = it.data
        }
        viewModel.loadNote(testData.id)
        noteLiveData.value = NoteResult.Success(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `should save note with filled text and title`(){
        viewModel.save(testTitle, testBody)
        verify(exactly = 1){ mockRepository.saveNote(any()) }
    }

    @Test
    fun `should edit note`(){
        var result: Note? = null
        viewModel.getViewState().observeForever{
            result = it.note
        }
        viewModel.loadNote(testData.id)
        viewModel.save(testTitle, testBody)
        testData.title = testTitle
        noteLiveData.value = NoteResult.Success(testData)
        assertTrue(result?.title == testTitle)
    }

    @Test
    fun `should dismiss empty note`(){
        viewModel.save("", "")
        verify (exactly = 0) {mockRepository.saveNote(any())}
    }
}