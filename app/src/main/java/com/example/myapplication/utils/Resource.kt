package com.example.myapplication.utils


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
data class Resource<T>(
    var message: String? = null,
    var loading: Boolean = false,
    var data: T? = null
)
{
    companion object {

        fun <T> error(message: String): Resource<T> {
            return Resource(
                message = message,
                loading = false,
                data = null
            )
        }

        fun <T> loading(isLoading: Boolean): Resource<T> {
            return Resource(
                message = null,
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(message: String? = null, data: T? = null): Resource<T> {
            return Resource(
                message = message,
                loading = false,
                data = data
            )
        }
    }

    override fun toString(): String {
        return "DataState(\nmessage=$message,\n loading=$loading,\n data=$data)"
    }
}
