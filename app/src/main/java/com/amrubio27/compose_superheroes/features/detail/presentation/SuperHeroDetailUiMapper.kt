package com.amrubio27.compose_superheroes.features.detail.presentation

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero

fun SuperHero.toDetailUiModel(): SuperHeroDetailUiModel {
    return SuperHeroDetailUiModel(
        id = this.id,
        name = this.name,
        slug = this.slug,
        imageUrl = this.images.lg
    )
}