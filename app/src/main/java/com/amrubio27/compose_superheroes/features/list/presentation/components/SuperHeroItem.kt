package com.amrubio27.compose_superheroes.features.list.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amrubio27.compose_superheroes.features.list.domain.SuperHero

@Composable
fun SuperheroItem(hero: SuperHero) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(hero.name, modifier = Modifier.padding(8.dp))
    }
}