package com.amrubio27.compose_superheroes.app.data.remote

import com.amrubio27.compose_superheroes.app.domain.ErrorApp
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <reified T> safeApiCall(crossinline call: suspend () -> HttpResponse): Result<T> {
    return try {
        val response = call.invoke()
        if (response.status.value in 200..299) {
            Result.success(response.body())
        } else {
            Result.failure(ErrorApp.ServerError)
        }
    } catch (exception: Throwable) {
        val error = when (exception) {
            is ConnectException -> ErrorApp.InternetError
            is UnknownHostException -> ErrorApp.ServerError
            is SocketTimeoutException -> ErrorApp.TimeoutError
            else -> ErrorApp.UnknownError
        }
        Result.failure(error)
    }
}
