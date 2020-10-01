package com.sweetmay.taskmanager

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.sweetmay.taskmanager.model.Note
import com.sweetmay.taskmanager.view.adapter.TaskAdapterRV
import com.sweetmay.taskmanager.view.ui.MainActivity
import com.sweetmay.taskmanager.view.ui.MainViewState
import com.sweetmay.taskmanager.view.ui.viewmodel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import java.util.*

class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java,  true, false)

    private val model: MainViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
        Note("1", "1", Date(), "1"),
        Note("2", "2", Date(), "2"),
        Note("3", "3", Date(), "3"),
        Note("4", "4", Date(), "4"),
        Note("5", "5", Date(), "5")
    )

    @Before
    fun setup(){
        loadKoinModules(
            listOf(
                module {
                    viewModel{model}
                }
            )
        )
        every { model.getViewState() } returns viewStateLiveData
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun teardown(){
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        onView(withId(R.id.notes_rv)).perform(scrollToPosition<TaskAdapterRV.ViewHolder>(1))
        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }

    }