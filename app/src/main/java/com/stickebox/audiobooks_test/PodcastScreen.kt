package com.stickebox.audiobooks_test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun PodcastScreen(modifier: Modifier = Modifier, podcasts: List<Podcast>) {
    Column {
        Text(text = stringResource(R.string.podcasts_title))
        PodcastList(podcasts = podcasts)
    }
}

@Composable
fun PodcastList(modifier: Modifier = Modifier, podcasts: List<Podcast>) {
    LazyColumn(modifier = modifier) {
        items(podcasts) { podcast ->
            key(podcast.id) {
                PodcastRow(podcast = podcast)
            }
        }
    }
}

@Composable
fun PodcastRow(modifier: Modifier = Modifier, podcast: Podcast) {
    Row(modifier = modifier.fillMaxWidth()) {
        // Null content description since the artwork is purely for visual purposes
        AsyncImage(model = podcast.imageUrl, contentDescription = null)
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(text = podcast.name)
            Text(text = podcast.description)
        }
    }
}
