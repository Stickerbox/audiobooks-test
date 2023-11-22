package com.stickebox.audiobooks_test.usecase

import com.stickebox.audiobooks_test.list.PodcastId
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.persistence.Database
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
