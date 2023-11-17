package com.stickebox.audiobooks_test.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.stickebox.audiobooks_test.models.Podcast

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
    @Json(name = "thumbnail")
    val description: String,
)

fun PodcastResponse.toDomainModel(isFavourite: Boolean = false): Podcast {
    return Podcast(
        id = id,
        imageUrl = image,
        name = title,
        description = description,
        thumbnailImageUrl = thumbnailImage,
        isFavourite = isFavourite
    )
}