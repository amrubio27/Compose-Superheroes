package com.amrubio27.compose_superheroes.features.list.data.remote

import com.amrubio27.compose_superheroes.features.list.domain.Appearance
import com.amrubio27.compose_superheroes.features.list.domain.Biography
import com.amrubio27.compose_superheroes.features.list.domain.Connections
import com.amrubio27.compose_superheroes.features.list.domain.Images
import com.amrubio27.compose_superheroes.features.list.domain.PowerStats
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.Work

fun SuperHeroApiModel.toModel(): SuperHero {
    return SuperHero(
        id = id,
        name = name,
        slug = slug,
        powerstats = powerstats.toModel(),
        appearance = appearance.toModel(),
        biography = biography.toModel(),
        work = work.toModel(),
        connections = connections.toModel(),
        images = images.toModel()
    )
}

fun PowerStatsApiModel.toModel(): PowerStats {
    return PowerStats(
        intelligence = intelligence,
        strength = strength,
        speed = speed,
        durability = durability,
        power = power,
        combat = combat
    )
}

fun AppearanceApiModel.toModel(): Appearance {
    return Appearance(
        gender = gender,
        race = race,
        height = height,
        weight = weight,
        eyeColor = eyeColor,
        hairColor = hairColor
    )
}

fun BiographyApimodel.toModel(): Biography {
    return Biography(
        fullName = fullName,
        alterEgos = alterEgos,
        aliases = aliases,
        placeOfBirth = placeOfBirth,
        firstAppearance = firstAppearance,
        publisher = publisher,
        alignment = alignment
    )
}

fun WorkApiModel.toModel(): Work {
    return Work(
        occupation = occupation,
        base = base
    )
}

fun ConnectionsApiModel.toModel(): Connections {
    return Connections(
        groupAffiliation = groupAffiliation,
        relatives = relatives
    )
}

fun ImagesApiModel.toModel(): Images {
    return Images(
        xs = xs,
        sm = sm,
        md = md,
        lg = lg
    )
}