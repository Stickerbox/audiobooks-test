package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first

/**
 * A local in-memory implementation of [Database].
 */
class InMemoryDatabase : Database {

    private val _podcasts: MutableSharedFlow<List<Podcast>> = MutableSharedFlow(0, 1)
    override val podcasts: SharedFlow<List<Podcast>> = _podcasts.asSharedFlow()

    /**
     * Saves the podcasts to a [StateFlow], mainly so that the [Podcast::isFavourite] state
     * can be flipped later on
     */
    override suspend fun save(podcasts: List<Podcast>) {
        val mutableList = _podcasts.first().toMutableList()
        mutableList.addAll(podcasts)

        _podcasts.emit(mutableList.toList())
    }

    /**
     * For an in-memory implementation, this just takes the podcast, flips is [isFavourite] boolean,
     * and updates [podcasts] with the new state.
     *
     * For a non-static API, this function could just save the results from an API request instead
     */
    override suspend fun updateFavouriteState(podcastId: String, isFavourite: Boolean) {
        val mutableList = _podcasts.first().toMutableList()
        val podcastToFavourite = mutableList.firstOrNull { it.id == podcastId } ?: return
        val updatedPodcast = podcastToFavourite.copy(isFavourite = isFavourite)
        val currentPosition = mutableList.indexOf(podcastToFavourite)
        mutableList.remove(podcastToFavourite)
        mutableList.add(currentPosition, updatedPodcast)

        _podcasts.emit(mutableList.toList())
    }
}
