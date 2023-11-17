package com.stickebox.audiobooks_test.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
