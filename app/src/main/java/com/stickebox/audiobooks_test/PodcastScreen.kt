package com.stickebox.audiobooks_test

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat

@Composable
fun PodcastListScreen(
    modifier: Modifier = Modifier,
    uiState: PodcastListScreenViewModel.PodcastListUiState,
    onEndOfList: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.podcasts_title),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black
        )
        PodcastList(podcasts = uiState.podcasts, onEndOfList = onEndOfList)
    }
}

@Composable
private fun PodcastList(
    modifier: Modifier = Modifier,
    podcasts: List<Podcast>,
    onEndOfList: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    if (!lazyListState.canScrollForward) {
        onEndOfList()
    }

    LazyColumn(modifier = modifier, state = lazyListState) {
        items(podcasts) { podcast ->
            key(podcast.id) {
                PodcastRow(podcast = podcast, onPodcastClicked = {

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
private fun PodcastRow(modifier: Modifier = Modifier, podcast: Podcast, onPodcastClicked: () -> Unit) {
    Row(
        modifier = modifier
            .clickable { onPodcastClicked() }
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

/**
 * Converts a [Spanned] into an [AnnotatedString] trying to keep as much formatting as possible.
 *
 * Currently supports `bold`, `italic`, `underline` and `color`.
 */
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ), start, end
                )
            }

            is UnderlineSpan -> addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start,
                end
            )

            is ForegroundColorSpan -> addStyle(
                SpanStyle(color = Color(span.foregroundColor)),
                start,
                end
            )
        }
    }
}