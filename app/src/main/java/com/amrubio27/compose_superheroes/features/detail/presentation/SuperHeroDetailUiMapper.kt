package com.amrubio27.compose_superheroes.features.detail.presentation

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero

fun SuperHero.toDetailUiModel(): SuperHeroDetailUiModel {
    return SuperHeroDetailUiModel(
        id = this.id,
        name = this.name,
        slug = this.slug,
        imageUrl = this.images.lg,
        powerStats = PowerStatsUi(
            intelligence = this.powerstats.intelligence,
            strength = this.powerstats.strength,
            speed = this.powerstats.speed,
            durability = this.powerstats.durability,
            power = this.powerstats.power,
            combat = this.powerstats.combat
        ),
        appearance = AppearanceUi(
            gender = this.appearance.gender,
            race = this.appearance.race ?: "Unknown",
            height = this.appearance.height?.get(1) ?: "Unknown", // Prefer cm
            weight = this.appearance.weight.getOrNull(1) ?: "Unknown", // Prefer kg
            eyeColor = this.appearance.eyeColor,
            hairColor = this.appearance.hairColor
        ),
        biography = BiographyUi(
            fullName = this.biography.fullName,
            alterEgos = this.biography.alterEgos,
            aliases = this.biography.aliases,
            placeOfBirth = this.biography.placeOfBirth,
            firstAppearance = this.biography.firstAppearance,
            publisher = this.biography.publisher ?: "Unknown",
            alignment = this.biography.alignment
        ),
        work = WorkUi(
            occupation = this.work.occupation,
            base = this.work.base
        ),
        connections = ConnectionsUi(
            groupAffiliation = this.connections.groupAffiliation,
            relatives = this.connections.relatives
        )
    )
}