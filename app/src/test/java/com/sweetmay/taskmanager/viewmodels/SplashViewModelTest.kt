package com.sweetmay.taskmanager.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sweetmay.taskmanager.model.Repository
import com.sweetmay.taskmanager.model.User
import com.sweetmay.taskmanager.model.errors.NoAuthException
import com.sweetmay.taskmanager.view.ui.SplashViewState
import com.sweetmay.taskmanager.view.ui.viewmodel.NoteViewModel
import com.sweetmay.taskmanager.view.ui.viewmodel.SplashViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>(relaxed = true)
    private val userLiveData = MutableLiveData<User?>()
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup(){
        clearAllMocks()
        viewModel = SplashViewModel(mockRepository)
        every { mockRepository.getCurrentUser() } returns userLiveData
    }

    @Test
    fun `should auth if user exists`(){
        var result: Boolean? = null
        viewModel.getViewState().observeForever{
            result = it.data
        }
        viewModel.requestUser()
        userLiveData.value = User("", "")
        assertEquals(result,  true)
    }

    @Test
    fun `should throw noAuth if current user wasn't found`(){
        var result: Throwable? = null
        viewModel.getViewState().observeForever{
            result = it.error
        }
        viewModel.requestUser()
        userLiveData.value = null
        assertTrue(result is NoAuthException)
    }
}