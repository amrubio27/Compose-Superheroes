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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amrubio27.compose_superheroes.features.detail.presentation.AppearanceUi
import com.amrubio27.compose_superheroes.features.detail.presentation.BiographyUi
import com.amrubio27.compose_superheroes.features.detail.presentation.ConnectionsUi
import com.amrubio27.compose_superheroes.features.detail.presentation.WorkUi

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun BiographyCard(bio: BiographyUi) {
    InfoCard {
        InfoItem("Full Name", bio.fullName)
        InfoItem("Alter Egos", bio.alterEgos)
        InfoItem("Place of Birth", bio.placeOfBirth)
        InfoItem("First Appearance", bio.firstAppearance)
        InfoItem("Publisher", bio.publisher)
        InfoItem("Alignment", bio.alignment.replaceFirstChar { it.uppercase() })
    }
}

@Composable
fun AppearanceCard(appearance: AppearanceUi) {
    InfoCard {
        InfoItem("Gender", appearance.gender)
        InfoItem("Race", appearance.race)
        InfoItem("Height", appearance.height)
        InfoItem("Weight", appearance.weight)
        InfoItem("Eye Color", appearance.eyeColor)
        InfoItem("Hair Color", appearance.hairColor)
    }
}

@Composable
fun WorkCard(work: WorkUi) {
    InfoCard {
        InfoItem("Occupation", work.occupation)
        InfoItem("Base", work.base)
    }
}

@Composable
fun ConnectionsCard(connections: ConnectionsUi) {
    InfoCard {
        InfoItem("Group Affiliation", connections.groupAffiliation)
        InfoItem("Relatives", connections.relatives)
    }
}

@Composable
fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}
