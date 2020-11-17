package com.example.myapplication.utils
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * An interceptor that allows runtime changes to the URL hostname.
 * Usually used in combination with MockWebServer.
 */

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        Timber.d(request.toString())
        return chain.proceed(request)
    }
}

/*
class BaseUrlInterceptor(private val realBaseUrl: String) : Interceptor {

    @Volatile private var host: String? = null

    fun setBaseUrl(host: String) {
        this.host = host
    }

    fun resetBaseUrl() {
        this.host = realBaseUrl
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (host != null && realBaseUrl != host) {
            val newUrl = host!!.toHttpUrlOrNull()
            request = request.newBuilder()
                .url(newUrl!!)
                .build()
        }
        return chain.proceed(request)
    }

}*/
