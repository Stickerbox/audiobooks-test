package com.stickebox.audiobooks_test.usecase

import com.stickebox.audiobooks_test.list.PodcastId
import com.stickebox.audiobooks_test.persistence.Database

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
