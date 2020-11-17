package com.example.myapplication.helpers

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.enqueueRequest(fileName: String, code: Int) {
    val responseBody = TestServiceHelper.getStringFromFile(fileName)
    val mockResponse = MockResponse()
    mockResponse.setResponseCode(code)
    mockResponse.setBody(responseBody)
    enqueue(mockResponse)
}