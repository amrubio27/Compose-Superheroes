package com.amrubio27.compose_superheroes.app.presentation.error

import android.content.Context
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.app.domain.ErrorApp

class ErrorMapper(private val context: Context) {

    fun map(error: ErrorApp): ErrorUiModel {
        return when (error) {
            ErrorApp.InternetError -> ErrorUiModel(
                image = R.drawable.ic_launcher_background, // TODO: Replace with proper error image
                title = context.getString(R.string.error_network_title),
                description = context.getString(R.string.error_network_description)
            )

            ErrorApp.ServerError -> ErrorUiModel(
                image = R.drawable.ic_launcher_background, // TODO: Replace with proper error image
                title = context.getString(R.string.error_server_title),
                description = context.getString(R.string.error_server_description)
            )

            ErrorApp.TimeoutError -> ErrorUiModel(
                image = R.drawable.ic_launcher_background, // TODO: Replace with proper error image
                title = context.getString(
                    R.string.error_unknown_title
                ), // Using unknown for timeout for now or create specific string
                description = context.getString(R.string.error_unknown_description)
            )

            ErrorApp.UnknownError -> ErrorUiModel(
                image = R.drawable.ic_launcher_background, // TODO: Replace with proper error image
                title = context.getString(R.string.error_unknown_title),
                description = context.getString(R.string.error_unknown_description)
            )

            ErrorApp.DataExpiredError -> ErrorUiModel(
                image = R.drawable.ic_launcher_background, // TODO: Replace with proper error image
                title = context.getString(R.string.error_unknown_title),
                description = context.getString(R.string.error_unknown_description)
            )
        }
    }
}
