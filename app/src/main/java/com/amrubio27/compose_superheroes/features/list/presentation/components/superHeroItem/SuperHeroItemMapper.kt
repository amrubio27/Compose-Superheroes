package com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem

import com.amrubio27.compose_superheroes.features.list.domain.SuperHero

fun SuperHero.toItemModel(): SuperHeroItemModel {
    return SuperHeroItemModel(
        id = id,
        name = name,
        slug = slug
    )
}