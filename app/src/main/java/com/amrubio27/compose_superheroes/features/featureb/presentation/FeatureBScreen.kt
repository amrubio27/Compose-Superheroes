package com.amrubio27.compose_superheroes.features.featureb.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.ui.theme.dimens

@Composable
fun FeatureBScreen() {
    Scaffold { padding ->
        val dimens = MaterialTheme.dimens
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = dimens.paddingMedium)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(top = dimens.paddingMedium))

            Text(
                stringResource(R.string.feature_b_title),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}