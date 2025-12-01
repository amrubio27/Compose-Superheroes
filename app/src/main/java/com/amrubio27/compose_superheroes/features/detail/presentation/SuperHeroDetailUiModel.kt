package com.amrubio27.compose_superheroes.features.detail.presentation

data class SuperHeroDetailUiModel(
    val id: Int,
    val name: String,
    val slug: String,
    val imageUrl: String,
    val powerStats: PowerStatsUi,
    val appearance: AppearanceUi,
    val biography: BiographyUi,
    val work: WorkUi,
    val connections: ConnectionsUi
)

data class PowerStatsUi(
    val intelligence: Int,
    val strength: Int,
    val speed: Int,
    val durability: Int,
    val power: Int,
    val combat: Int
)

data class AppearanceUi(
    val gender: String,
    val race: String,
    val height: String,
    val weight: String,
    val eyeColor: String,
    val hairColor: String
)

data class BiographyUi(
    val fullName: String,
    val alterEgos: String,
    val aliases: List<String>,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)

data class WorkUi(
    val occupation: String,
    val base: String
)

data class ConnectionsUi(
    val groupAffiliation: String,
    val relatives: String
)
