package com.amrubio27.compose_superheroes.features.list.domain

import kotlinx.serialization.Serializable

@Serializable
data class SuperHero(
    val id: Int,
    val name: String,
    val slug: String,
    val powerstats: PowerStats,
    val appearance: Appearance,
    val biography: Biography,
    val work: Work,
    val connections: Connections,
    val images: Images
)

@Serializable
data class PowerStats(
    val intelligence: Int,
    val strength: Int,
    val speed: Int,
    val durability: Int,
    val power: Int,
    val combat: Int
)

@Serializable
data class Appearance(
    val gender: String,
    val race: String?,
    val height: List<String>?,
    val weight: List<String>,
    val eyeColor: String,
    val hairColor: String

)

@Serializable
data class Biography(
    val fullName: String,
    val alterEgos: String,
    val aliases: List<String>,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String?,
    val alignment: String
)

@Serializable
data class Work(
    val occupation: String,
    val base: String
)

@Serializable
data class Connections(
    val groupAffiliation: String,
    val relatives: String
)

@Serializable
data class Images(
    val xs: String,
    val sm: String,
    val md: String,
    val lg: String
)
