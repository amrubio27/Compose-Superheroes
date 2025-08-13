package com.amrubio27.compose_superheroes.app.domain

sealed class ErrorApp : Exception() {
    data object InternetError : ErrorApp()
    data object ServerError : ErrorApp()
    data object TimeoutError : ErrorApp()
    data object UnknownError : ErrorApp()
}