package com.amrubio27.compose_superheroes.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Detail(
    val id: Int
)

@Serializable
object FeatureB


