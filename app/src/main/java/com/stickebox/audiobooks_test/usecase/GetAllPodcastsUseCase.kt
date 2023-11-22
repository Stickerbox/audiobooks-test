package com.stickebox.audiobooks_test.usecase

import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.persistence.Database
import kotlinx.coroutines.flow.Flow

class GetAllPodcastsUseCase(
    private val database: Database
) {

    fun execute(): Flow<List<Podcast>> {
        return database.podcasts
    }
}
