package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast

/**
 * Gets the next page of podcasts based on [PodcastPaginationState]
 *
 * @param repository The [PodcastRepository] from which to grab the next page
 */
class LoadPodcastsUseCase(
    private val repository: PodcastRepository,
) {

    /**
     * Executes the load and returns whether the page successfully loaded into memory.
     * If this is successful, the output will be sent through to [Database.podcasts].
     *
     * @param paginationState The next [PodcastPaginationState] state to fetch
     *
     * @return Whether the page of podcasts requested could be successfully retrieved
     */
    suspend fun execute(paginationState: PodcastPaginationState): LoadPodcastsResult {
        val result = repository.loadNextPage(paginationState)
        return if (result.isSuccess) {
            LoadPodcastsResult.Success(result.getOrThrow())
        } else {
            LoadPodcastsResult.Failure(result.exceptionOrNull())
        }
    }

    sealed class LoadPodcastsResult {
        data class Success(val result: List<Podcast>) : LoadPodcastsResult()
        data class Failure(val throwable: Throwable?) : LoadPodcastsResult()
    }
}
