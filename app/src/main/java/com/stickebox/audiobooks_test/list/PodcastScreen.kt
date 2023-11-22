package com.stickebox.audiobooks_test.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stickebox.audiobooks_test.models.Podcast
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.stickebox.audiobooks_test.R

internal typealias PodcastId = String

@Composable
fun PodcastListScreen(
    modifier: Modifier = Modifier,
    uiState: PodcastListScreenViewModel.PodcastListUiState,
    onEndOfList: () -> Unit,
    onPodcastClicked: (PodcastId) -> Unit
) {
    Box(modifier = modifier) {
        Column {
            Text(
                text = stringResource(R.string.podcasts_title),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            PodcastList(
                podcasts = uiState.podcasts,
                onEndOfList = onEndOfList,
                onPodcastClicked = onPodcastClicked
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun PodcastList(
    modifier: Modifier = Modifier,
    podcasts: List<Podcast>,
    onEndOfList: () -> Unit,
    onPodcastClicked: (PodcastId) -> Unit
) {
    val lazyListState = rememberLazyListState()

    if (!lazyListState.canScrollForward) {
        onEndOfList()
    }

    LazyColumn(modifier = modifier, state = lazyListState) {
        items(podcasts) { podcast ->
            key(podcast.id) {
                PodcastRow(podcast = podcast, onPodcastClicked = {
                    onPodcastClicked(it)
                })
                Divider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun PodcastRow(
    modifier: Modifier = Modifier,
    podcast: Podcast,
    onPodcastClicked: (PodcastId) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onPodcastClicked(podcast.id) }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Null content description since the artwork is purely for visual purposes
        AsyncImage(
            model = podcast.thumbnailImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = podcast.name, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = podcast.publisher,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            if (podcast.isFavourite) {
                Text(text = stringResource(R.string.podcast_list_favourited))
            }
        }
    }
}
