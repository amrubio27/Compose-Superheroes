package com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amrubio27.compose_superheroes.ui.theme.dimens

@Composable
fun SuperheroItem(hero: SuperHeroItemModel, navigateToDetail: (Int) -> Unit) {
    val dimens = MaterialTheme.dimens
    Card(
        modifier = Modifier
            .padding(dimens.paddingExtraSmall)
            .fillMaxWidth()
            .height(dimens.listItemHeight)
            .clickable(
                onClick = { navigateToDetail(hero.id) }
            ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(dimens.elevationSmall)
    ) {
        Row(modifier = Modifier.padding(dimens.paddingSmall)) {
            Text(hero.id.toString(), modifier = Modifier)
            Spacer(modifier = Modifier.width(dimens.paddingMedium))
            Text(hero.name, modifier = Modifier)
            Spacer(modifier = Modifier.weight(1f))
            Text(hero.slug, modifier = Modifier)
        }
    }
}