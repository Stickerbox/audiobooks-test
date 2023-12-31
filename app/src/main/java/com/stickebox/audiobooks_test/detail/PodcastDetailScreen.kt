package com.stickebox.audiobooks_test.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stickebox.audiobooks_test.R
import com.stickebox.audiobooks_test.ui.theme.ButtonPrimary

@Composable
fun PodcastDetailScreen(
    modifier: Modifier = Modifier,
    onFavouritePodcast: (Boolean) -> Unit,
    uiState: PodcastDetailViewModel.PodcastDetailUiState,
    onBackPressed: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.podcast_details_back),
            modifier = Modifier
                .clickable { onBackPressed() }
                .padding(16.dp)
        )

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            SmallSpacer()
            Text(
                text = uiState.publisher,
                style = MaterialTheme.typography.labelLarge,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.alpha(0.5f)
            )
            LargeSpacer()
            AsyncImage(
                model = uiState.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            SmallSpacer()
            Button(
                onClick = {
                    onFavouritePodcast(!uiState.isFavourite)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
            ) {
                val text = if (!uiState.isFavourite) {
                    stringResource(R.string.podcast_detail_favourite)
                } else {
                    stringResource(R.string.podcast_detail_unfavourite)
                }
                Text(text = text)
            }
            LargeSpacer()
            Text(
                text = uiState.description,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

@Composable
private fun SmallSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun LargeSpacer() {
    Spacer(modifier = Modifier.height(20.dp))
}