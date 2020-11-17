package com.example.myapplication.helpers

import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Helper class to load file with network responses from "assets" folder
 * in androidTest dir.
 */
object TestServiceHelper {
    @Throws(Exception::class)
    fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    @Throws(Exception::class)
    fun getStringFromFile(filePath: String): String {
        val stream: InputStream =
            InstrumentationRegistry.getInstrumentation().getContext().getAssets().open(filePath)
        val ret = convertStreamToString(stream)
        //Make sure you close all streams.
        stream.close()
        return ret
    }
}