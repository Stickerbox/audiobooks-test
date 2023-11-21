package com.stickebox.audiobooks_test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.ui.theme.ButtonPrimary

@Composable
fun PodcastDetailScreen(
    modifier: Modifier = Modifier,
    onFavouritePodcast: (Boolean) -> Unit,
    uiState: PodcastDetailViewModel.PodcastDetailUiState
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = uiState.publisher)
        AsyncImage(
            model = uiState.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(24.dp))
        )
        Button(
            onClick = {
                onFavouritePodcast(!uiState.isFavourite)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary)
        ) {
            val text = if (!uiState.isFavourite) {
                "Favourite"
            } else {
                "Unfavourite"
            }
            Text(text = text)
        }
        Text(text = uiState.description)
    }
}
