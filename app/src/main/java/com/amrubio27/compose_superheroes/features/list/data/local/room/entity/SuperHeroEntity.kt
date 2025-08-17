package com.amrubio27.compose_superheroes.features.list.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.AppearanceConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.BiographyConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.ConnectionsConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.ImagesConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.PowerStatsConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.WorkConverter
import com.amrubio27.compose_superheroes.features.list.domain.Appearance
import com.amrubio27.compose_superheroes.features.list.domain.Biography
import com.amrubio27.compose_superheroes.features.list.domain.Connections
import com.amrubio27.compose_superheroes.features.list.domain.Images
import com.amrubio27.compose_superheroes.features.list.domain.PowerStats
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.amrubio27.compose_superheroes.features.list.domain.Work

@Entity(tableName = "superheroes")
@TypeConverters(
    PowerStatsConverter::class,
    AppearanceConverter::class,
    BiographyConverter::class,
    WorkConverter::class,
    ConnectionsConverter::class,
    ImagesConverter::class
)
data class SuperHeroEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val slug: String,
    val powerstats: PowerStats,
    val appearance: Appearance,
    val biography: Biography,
    val work: Work,
    val connections: Connections,
    val images: Images,
    val timeStamp: Long
) {
    fun toDomain(): SuperHero {
        return SuperHero(
            id = id,
            name = name,
            slug = slug,
            powerstats = powerstats,
            appearance = appearance,
            biography = biography,
            work = work,
            connections = connections,
            images = images
        )
    }

    companion object {
        fun toEntity(
            superHero: SuperHero,
            timeStamp: Long = System.currentTimeMillis()
        ): SuperHeroEntity {
            return SuperHeroEntity(
                id = superHero.id,
                name = superHero.name,
                slug = superHero.slug,
                powerstats = superHero.powerstats,
                appearance = superHero.appearance,
                biography = superHero.biography,
                work = superHero.work,
                connections = superHero.connections,
                images = superHero.images,
                timeStamp = timeStamp
            )
        }
    }
}
