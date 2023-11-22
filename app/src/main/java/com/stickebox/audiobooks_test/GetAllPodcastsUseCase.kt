package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow

class GetAllPodcastsUseCase(
    private val database: Database
) {

    fun execute(): Flow<List<Podcast>> {
        return database.podcasts
    }
}
