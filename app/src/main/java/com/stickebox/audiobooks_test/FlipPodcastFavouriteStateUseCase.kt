package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast

class FlipPodcastFavoriteStateUseCase(
    private val repository: PodcastRepository
) {
    suspend fun execute(podcast: Podcast) {
        repository.switchPodcastFavouriteState(podcast)
    }
}