package com.amrubio27.compose_superheroes.features.list.data.local.room.converter

import androidx.room.TypeConverter
import com.amrubio27.compose_superheroes.features.list.domain.Appearance
import com.amrubio27.compose_superheroes.features.list.domain.Biography
import com.amrubio27.compose_superheroes.features.list.domain.Connections
import com.amrubio27.compose_superheroes.features.list.domain.Images
import com.amrubio27.compose_superheroes.features.list.domain.PowerStats
import com.amrubio27.compose_superheroes.features.list.domain.Work
import kotlinx.serialization.json.Json

class PowerStatsConverter {
    @TypeConverter
    fun fromPowerStats(powerstats: PowerStats): String {
        return Json.encodeToString(powerstats)
    }

    @TypeConverter
    fun toPowerStats(powerstatsJson: String): PowerStats {
        return Json.decodeFromString(powerstatsJson)
    }
}

class AppearanceConverter {
    @TypeConverter
    fun fromAppearance(appearance: Appearance): String {
        return Json.encodeToString(appearance)
    }

    @TypeConverter
    fun toAppearance(appearanceJson: String): Appearance {
        return Json.decodeFromString(appearanceJson)
    }
}

class BiographyConverter {
    @TypeConverter
    fun fromBiography(biography: Biography): String {
        return Json.encodeToString(biography)
    }

    @TypeConverter
    fun toBiography(biographyJson: String): Biography {
        return Json.decodeFromString(biographyJson)
    }
}

class WorkConverter {
    @TypeConverter
    fun fromWork(work: Work): String {
        return Json.encodeToString(work)
    }

    @TypeConverter
    fun toWork(workJson: String): Work {
        return Json.decodeFromString(workJson)
    }
}

class ConnectionsConverter {
    @TypeConverter
    fun fromConnections(connections: Connections): String {
        return Json.encodeToString(connections)
    }

    @TypeConverter
    fun toConnections(connectionsJson: String): Connections {
        return Json.decodeFromString(connectionsJson)
    }
}

class ImagesConverter {
    @TypeConverter
    fun fromImages(images: Images): String {
        return Json.encodeToString(images)
    }

    @TypeConverter
    fun toImages(imagesJson: String): Images {
        return Json.decodeFromString(imagesJson)
    }
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.let { Json.encodeToString(it) } ?: "[]"
    }

    @TypeConverter
    fun toStringList(listJson: String): List<String>? {
        return if (listJson == "[]" || listJson.isBlank()) null
        else Json.decodeFromString(listJson)
    }
}
