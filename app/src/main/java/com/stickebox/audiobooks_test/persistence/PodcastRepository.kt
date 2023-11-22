package com.stickebox.audiobooks_test.persistence

import com.stickebox.audiobooks_test.list.PodcastPaginationState
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.network.PodcastApiService
import com.stickebox.audiobooks_test.network.models.toDomainModel

class PodcastRepository(
    private val apiService: PodcastApiService,
) {

    suspend fun loadNextPage(nextState: PodcastPaginationState): Result<List<Podcast>> {
        val podcastsResponse = apiService.getBestPodcasts(nextState.page)
        if (!podcastsResponse.isSuccessful) {
            return Result.failure(Throwable("Unable to load podcasts with state $nextState"))
        }

        val podcasts = podcastsResponse.body()?.podcasts?.map { it.toDomainModel() }
            ?: return Result.failure(Throwable("Unable to load podcasts with state $nextState"))

        return Result.success(podcasts)
    }
}
