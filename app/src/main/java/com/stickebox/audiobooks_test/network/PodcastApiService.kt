package com.stickebox.audiobooks_test.network

import com.stickebox.audiobooks_test.network.models.BestPodcastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastApiService {

    @GET("best_podcasts")
    suspend fun getBestPodcasts(@Query("page") page: Int): Response<BestPodcastResponse>
}
