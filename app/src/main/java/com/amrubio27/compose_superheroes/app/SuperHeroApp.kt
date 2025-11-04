package com.amrubio27.compose_superheroes.app

import android.app.Application
import com.amrubio27.compose_superheroes.di.AppModule
import com.amrubio27.compose_superheroes.di.DataStoreModule
import com.amrubio27.compose_superheroes.di.NetworkModule
import com.amrubio27.compose_superheroes.di.RoomDatabaseModule
import com.amrubio27.compose_superheroes.features.detail.di.SuperHeroDetailModule
import com.amrubio27.compose_superheroes.features.list.di.SuperHeroModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class SuperHeroApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SuperHeroApp)
            modules(
                AppModule().module,
                NetworkModule().module,
                DataStoreModule().module,
                RoomDatabaseModule().module,
                SuperHeroModule().module,
                SuperHeroDetailModule().module
            )
        }
    }
}