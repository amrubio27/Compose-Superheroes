package com.amrubio27.compose_superheroes.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.AppearanceConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.BiographyConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.ConnectionsConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.ImagesConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.PowerStatsConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.StringListConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.converter.WorkConverter
import com.amrubio27.compose_superheroes.features.list.data.local.room.dao.SuperHeroDao
import com.amrubio27.compose_superheroes.features.list.data.local.room.entity.SuperHeroEntity

@Database(
    entities = [SuperHeroEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    PowerStatsConverter::class,
    AppearanceConverter::class,
    BiographyConverter::class,
    WorkConverter::class,
    ConnectionsConverter::class,
    ImagesConverter::class,
    StringListConverter::class
)
abstract class SuperHeroDatabase : RoomDatabase() {
    abstract fun superHeroDao(): SuperHeroDao
}