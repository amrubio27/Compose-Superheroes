package com.amrubio27.compose_superheroes.features.list.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SuperHeroApiModel(
    val id: Int,
    val name: String,
    val slug: String,
    val powerstats: PowerStatsApiModel,
    val appearance: AppearanceApiModel,
    val biography: BiographyApimodel,
    val work: WorkApiModel,
    val connections: ConnectionsApiModel,
    val images: ImagesApiModel

)

@Serializable
data class PowerStatsApiModel(
    val intelligence: Int,
    val strength: Int,
    val speed: Int,
    val durability: Int,
    val power: Int,
    val combat: Int
)

@Serializable
data class AppearanceApiModel(
    val gender: String,
    val race: String?,
    val height: List<String>?,
    val weight: List<String>,
    val eyeColor: String,
    val hairColor: String

)

@Serializable
data class BiographyApimodel(
    val fullName: String,
    val alterEgos: String,
    val aliases: List<String>,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String?,
    val alignment: String
)

@Serializable
data class WorkApiModel(
    val occupation: String,
    val base: String
)

@Serializable
data class ConnectionsApiModel(
    val groupAffiliation: String,
    val relatives: String
)

@Serializable
data class ImagesApiModel(
    val xs: String,
    val sm: String,
    val md: String,
    val lg: String
)