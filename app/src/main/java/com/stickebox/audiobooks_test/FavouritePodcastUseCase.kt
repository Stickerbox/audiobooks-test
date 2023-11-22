package com.stickebox.audiobooks_test

/**
 * Updates the favourite state of a Podcast
 */
class FavouritePodcastUseCase(
    private val database: Database
) {
    suspend fun execute(podcastId: PodcastId, isFavourite: Boolean) {
        database.updateFavouriteState(podcastId = podcastId, isFavourite = isFavourite)
    }
}
