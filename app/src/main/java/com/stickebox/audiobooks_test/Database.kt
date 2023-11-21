package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * An abstract definition of a database
 */
interface Database {
    /**
     * A [StateFlow] of the current [List] of [Podcast] that has been collected and saved so far
     */
    val podcasts: Flow<List<Podcast>>

    /**
     * Saves a list of podcasts to a local store
     *
     * @param podcasts The list of [Podcast] models to save
     */
    suspend fun save(podcasts: List<Podcast>)

    /**
     * Updates the favourite state of a [Podcast]. If the remote API supported a dynamic
     * list of podcasts, this value could potentially instead come from the result of an
     * API request. For a static API, this state change needs to be locally maintained.
     *
     * @param podcastId The ID of the podcast to mutate
     * @param isFavourite The new state of whether the podcast has been favourited
     */
    suspend fun updateFavouriteState(podcastId: String, isFavourite: Boolean)
}
