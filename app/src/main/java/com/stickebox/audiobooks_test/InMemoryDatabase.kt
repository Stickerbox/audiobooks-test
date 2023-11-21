package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * A local in-memory implementation of [Database].
 */
class InMemoryDatabase : Database {

    private val _podcasts = MutableStateFlow<MutableList<Podcast>>(mutableListOf())
    override val podcasts: Flow<List<Podcast>> = _podcasts

    /**
     * Saves the podcasts to a [StateFlow], mainly so that the [Podcast::isFavourite] state
     * can be flipped later on
     */
    override suspend fun save(podcasts: List<Podcast>) {
        val currentList = _podcasts.value
        currentList.addAll(podcasts)
        _podcasts.emit(currentList)
    }

    /**
     * For an in-memory implementation, this just takes the podcast, flips is [isFavourite] boolean,
     * and updates [podcasts] with the new state.
     *
     * For a non-static API, this function could just save the results from an API request instead
     */
    override suspend fun updateFavouriteState(podcastId: String, isFavourite: Boolean) {
        val currentList = _podcasts.first()
        val podcastToFavourite = currentList.firstOrNull { it.id == podcastId } ?: return
        val updatedPodcast = podcastToFavourite.copy(isFavourite = isFavourite)
        val currentPosition = currentList.indexOf(podcastToFavourite)
        currentList.remove(podcastToFavourite)
        currentList.add(currentPosition, updatedPodcast)
        _podcasts.emit(currentList)
    }

    override fun fetchPodcast(id: PodcastId): Flow<Podcast> {
        return podcasts.map {
            it.first { podcast ->
                podcast.id == id
            }
        }
    }
}
