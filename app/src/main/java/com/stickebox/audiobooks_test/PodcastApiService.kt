package com.stickebox.audiobooks_test

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET


interface PodcastApiService {

    @GET("/best_podcasts")
    suspend fun getBestPodcasts(page: Int): BestPodcastResponse
}

@JsonClass(generateAdapter = true)
data class BestPodcastResponse(
    @Json(name = "has_next")
    val hasNext: Boolean,
    @Json(name = "podcasts")
    val podcasts: List<PodcastResponse>,
    @Json(name = "page_number")
    val pageNumber: Int,
    @Json(name = "next_page_number")
    val nextPageNumber: Int
)

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