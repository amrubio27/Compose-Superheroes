package com.amrubio27.compose_superheroes.di

import android.app.Application
import androidx.room.Room
import com.amrubio27.compose_superheroes.app.data.local.SuperHeroDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.amrubio27.compose_superheroes.features.list.data.local.room")
class RoomDatabaseModule {

    @Single
    fun provideSuperHeroDatabase(app: Application): SuperHeroDatabase {
        return Room.databaseBuilder(
            app,
            SuperHeroDatabase::class.java,
            "superheroes_database"
        ).build()
    }

}
