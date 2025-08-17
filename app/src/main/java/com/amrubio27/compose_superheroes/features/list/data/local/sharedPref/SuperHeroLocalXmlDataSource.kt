package com.amrubio27.compose_superheroes.features.list.data.local.sharedPref

import android.content.Context
import androidx.core.content.edit
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero
import com.google.gson.Gson
import org.koin.core.annotation.Single

interface SuperHeroLocalDataSource {
    fun getAll(): List<SuperHero>
    fun getHeroById(id: Int): SuperHero?
    fun saveAll(superHeroes: List<SuperHero>)
    fun saveByHero(superHero: SuperHero)
    fun delete()
}

@Single
class SuperHeroLocalXmlDataSource(
    context: Context
) : SuperHeroLocalDataSource {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    private val gson = Gson()

    override fun saveAll(superHeroes: List<SuperHero>) {
        sharedPref.edit {
            clear()
            superHeroes.forEach { superHero ->
                putString(superHero.id.toString(), gson.toJson(superHero))
            }
        }
    }

    override fun saveByHero(superHero: SuperHero) {
        sharedPref.edit {
            putString(superHero.id.toString(), gson.toJson(superHero))
        }
    }


    override fun getAll(): List<SuperHero> {
        return sharedPref.all.toMap().values.mapNotNull { jsonSuperHero ->
            gson.fromJson(jsonSuperHero as String, SuperHero::class.java)
        }
    }

    override fun getHeroById(id: Int): SuperHero? {
        val jsonSuperHero = sharedPref.getString(id.toString(), null)
        return if (jsonSuperHero != null) {
            gson.fromJson(jsonSuperHero, SuperHero::class.java)
        } else {
            null
        }
    }

    override fun delete() {
        sharedPref.edit {
            clear()
        }
    }
}

