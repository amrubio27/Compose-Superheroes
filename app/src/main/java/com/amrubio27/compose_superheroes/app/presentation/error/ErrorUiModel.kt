package com.amrubio27.compose_superheroes.app.presentation.error

import androidx.annotation.DrawableRes

data class ErrorUiModel(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)
