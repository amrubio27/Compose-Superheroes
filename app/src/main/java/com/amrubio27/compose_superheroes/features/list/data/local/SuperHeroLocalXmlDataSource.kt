package com.amrubio27.compose_superheroes.features.list.data.local

import android.content.Context
import androidx.core.content.edit
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.google.gson.Gson
import org.koin.core.annotation.Single

@Single
class SuperHeroLocalXmlDataSource(
    context: Context
) {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    private val gson = Gson()

    fun saveAll(superHeroes: List<SuperHero>) {
        sharedPref.edit {
            superHeroes.forEach { superHero ->
                putString(superHero.id.toString(), gson.toJson(superHero))
            }
        }
    }

    fun getAll(): List<SuperHero> {
        return sharedPref.all.toMap().values.mapNotNull { jsonSuperHero ->
            gson.fromJson(jsonSuperHero as String, SuperHero::class.java)
        }
    }

    fun delete() {
        sharedPref.edit {
            clear()
        }
    }
}