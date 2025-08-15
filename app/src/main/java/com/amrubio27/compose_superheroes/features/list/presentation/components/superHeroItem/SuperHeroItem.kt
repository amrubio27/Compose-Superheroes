package com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SuperheroItem(hero: SuperHeroItemModel, navigateToDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clickable(
                onClick = { navigateToDetail(hero.id) }
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(hero.id.toString(), modifier = Modifier)
            Spacer(modifier = Modifier.width(16.dp))
            Text(hero.name, modifier = Modifier)
            Spacer(modifier = Modifier.weight(1f))
            Text(hero.slug, modifier = Modifier)
        }
    }
}