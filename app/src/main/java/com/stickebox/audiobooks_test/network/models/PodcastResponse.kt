package com.stickebox.audiobooks_test.network.models

import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.utils.toAnnotatedString

@JsonClass(generateAdapter = true)
data class PodcastResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "thumbnail")
    val thumbnailImage: String,
    @Json(name = "description")
    val description: String,
)

fun PodcastResponse.toDomainModel(isFavourite: Boolean = false): Podcast {
    return Podcast(
        id = id,
        imageUrl = image,
        name = title,
        description = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT).toAnnotatedString(),
        thumbnailImageUrl = thumbnailImage,
        isFavourite = isFavourite,
        publisher = publisher
    )
}