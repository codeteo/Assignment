package com.example.myapplication.helpers

import okhttp3.mockwebserver.MockWebServer
import java.io.IOException


/**
 * Helper class for integration tests.
 *
 *
 * By changing the base URL, mockWebServer intercepts all network calls and
 * returns a fake response we provide from a json file.
 */
class MockWebServerHelper {

    @Throws(IOException::class)
    fun initMockWebServer(): MockWebServer {
        val mockWebServer = MockWebServer()
        mockWebServer.start()
        return mockWebServer
    }

}