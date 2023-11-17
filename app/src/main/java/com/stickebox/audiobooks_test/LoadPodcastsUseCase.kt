package com.stickebox.audiobooks_test

/**
 * Gets the next page of podcasts based on [PodcastPaginationState]
 *
 * @param repository The [PodcastRepository] from which to grab the next page
 */
class LoadPodcastsUseCase(
    private val repository: PodcastRepository
) {
    suspend fun execute(paginationState: PodcastPaginationState) {
        val result = repository.loadNextPage(paginationState)
        // Might have API error
    }
}