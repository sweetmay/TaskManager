package com.sweetmay.taskmanager.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.model.NoteResult
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.view.ui.viewmodel.MainViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`(){
        verify(exactly = 1){mockRepository.getNotes()}
    }

    @Test
    fun `should return notes`(){
        var result: List<Note>? = null
        val testData = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))
        viewModel.getViewState().observeForever{
            result = it.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testError = Throwable("error")
            viewModel.getViewState().observeForever{
                result = it.error
            }
        notesLiveData.value = NoteResult.Error(testError)
        assertEquals(result, testError)
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }
}