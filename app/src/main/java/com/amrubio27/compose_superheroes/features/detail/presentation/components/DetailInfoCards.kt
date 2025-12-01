package com.amrubio27.compose_superheroes.features.detail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.features.detail.presentation.AppearanceUi
import com.amrubio27.compose_superheroes.features.detail.presentation.BiographyUi
import com.amrubio27.compose_superheroes.features.detail.presentation.ConnectionsUi
import com.amrubio27.compose_superheroes.features.detail.presentation.WorkUi
import com.amrubio27.compose_superheroes.ui.theme.dimens

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(
            horizontal = MaterialTheme.dimens.paddingMedium,
            vertical = MaterialTheme.dimens.paddingSmall
        )
    )
}

@Composable
fun BiographyCard(bio: BiographyUi) {
    InfoCard {
        InfoItem(stringResource(R.string.detail_bio_full_name), bio.fullName)
        InfoItem(stringResource(R.string.detail_bio_alter_egos), bio.alterEgos)
        InfoItem(stringResource(R.string.detail_bio_place_of_birth), bio.placeOfBirth)
        InfoItem(stringResource(R.string.detail_bio_first_appearance), bio.firstAppearance)
        InfoItem(stringResource(R.string.detail_bio_publisher), bio.publisher)
        InfoItem(
            stringResource(R.string.detail_bio_alignment),
            bio.alignment.replaceFirstChar { it.uppercase() })
    }
}

@Composable
fun AppearanceCard(appearance: AppearanceUi) {
    InfoCard {
        InfoItem(stringResource(R.string.detail_appearance_gender), appearance.gender)
        InfoItem(stringResource(R.string.detail_appearance_race), appearance.race)
        InfoItem(stringResource(R.string.detail_appearance_height), appearance.height)
        InfoItem(stringResource(R.string.detail_appearance_weight), appearance.weight)
        InfoItem(stringResource(R.string.detail_appearance_eye_color), appearance.eyeColor)
        InfoItem(stringResource(R.string.detail_appearance_hair_color), appearance.hairColor)
    }
}

@Composable
fun WorkCard(work: WorkUi) {
    InfoCard {
        InfoItem(stringResource(R.string.detail_work_occupation), work.occupation)
        InfoItem(stringResource(R.string.detail_work_base), work.base)
    }
}

@Composable
fun ConnectionsCard(connections: ConnectionsUi) {
    InfoCard {
        InfoItem(
            stringResource(R.string.detail_connections_group_affiliation),
            connections.groupAffiliation
        )
        InfoItem(stringResource(R.string.detail_connections_relatives), connections.relatives)
    }
}

@Composable
fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.paddingMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(MaterialTheme.dimens.paddingMedium)
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingMediumSmall)
        ) {
            content()
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingExtraSmall))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}
