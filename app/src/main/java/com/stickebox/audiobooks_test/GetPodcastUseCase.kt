package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetPodcastUseCase(
    private val database: Database
) {

    fun execute(podcastId: PodcastId): Flow<Podcast> {
        return database.fetchPodcast(podcastId)
            .distinctUntilChanged()
    }
}
