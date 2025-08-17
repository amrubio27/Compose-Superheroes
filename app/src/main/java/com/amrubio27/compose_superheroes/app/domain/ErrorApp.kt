package com.amrubio27.compose_superheroes.app.domain

sealed class ErrorApp : Exception() {
    data object InternetError : ErrorApp() {
        private fun readResolve(): Any = InternetError
    }

    data object ServerError : ErrorApp() {
        private fun readResolve(): Any = ServerError
    }

    data object TimeoutError : ErrorApp() {
        private fun readResolve(): Any = TimeoutError
    }

    data object UnknownError : ErrorApp() {
        private fun readResolve(): Any = UnknownError
    }

    data object DataExpiredError : ErrorApp() {
        private fun readResolve(): Any = DataExpiredError
    }
}