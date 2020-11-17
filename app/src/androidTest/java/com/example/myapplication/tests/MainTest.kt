package com.example.myapplication.tests

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.example.myapplication.R
import com.example.myapplication.di.DatabaseModule
import com.example.myapplication.di.NetworkModule
import com.example.myapplication.helpers.MockWebServerHelper
import com.example.myapplication.helpers.TestServiceHelper.getStringFromFile
import com.example.myapplication.ui.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@UninstallModules(
    NetworkModule::class,
    DatabaseModule::class
)
@HiltAndroidTest
class MainTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    @Rule
    @JvmField
    var testRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun init() {
        hiltRule.inject()

        server = MockWebServerHelper().initMockWebServer()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun someTest() {
        // Given
        enqueueResponse()
        testRule.launchActivity(Intent())

        // When
        onView(withId(R.id.button))
            .perform(click())

        //Then
        //verify something
    }

    private fun enqueueResponse() {
        val questionsResponse = "responses/questions_response_200.json"

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(questionsResponse))
        )
    }

}