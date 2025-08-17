package com.amrubio27.compose_superheroes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataStoreModule {

    @Single
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("superhero_preferences")
            }
        )
    }
}
