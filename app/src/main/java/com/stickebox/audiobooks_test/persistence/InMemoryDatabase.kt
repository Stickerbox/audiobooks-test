package com.stickebox.audiobooks_test.persistence

import com.stickebox.audiobooks_test.list.PodcastId
import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

/**
 * A local in-memory implementation of [Database].
 */
class InMemoryDatabase : Database {

    // The podcast data
    private val podcastBackingData = mutableListOf<Podcast>()

    // The Flow that can be subscribed to
    private val _podcasts = MutableSharedFlow<List<Podcast>>(replay = 1)
    override val podcasts: Flow<List<Podcast>> = _podcasts

    /**
     * Saves the podcasts to a [StateFlow], mainly so that the [Podcast::isFavourite] state
     * can be flipped later on
     */
    override suspend fun save(podcasts: List<Podcast>) {
        val currentList = podcastBackingData
        currentList.addAll(podcasts)
        _podcasts.emit(currentList.distinctBy { it.id })
    }

    /**
     * For an in-memory implementation, this just takes the podcast, flips is [isFavourite] boolean,
     * and updates [podcasts] with the new state.
     *
     * For a non-static API, this function could just save the results from an API request instead
     */
    override suspend fun updateFavouriteState(podcastId: String, isFavourite: Boolean) {
        val currentList = podcastBackingData
        val podcastToFavourite = currentList.firstOrNull { it.id == podcastId } ?: return
        val updatedPodcast = podcastToFavourite.copy(isFavourite = isFavourite)
        val currentPosition = currentList.indexOf(podcastToFavourite)
        currentList.remove(podcastToFavourite)
        currentList.add(currentPosition, updatedPodcast)
        _podcasts.emit(currentList.distinctBy { it.id })
    }

    /**
     * Fetches a Flow of a specific [Podcast] by its ID
     *
     * @param id The String id of the podcast
     *
     * @return A Flow of [Podcast] as it changes over time
     */
    override fun fetchPodcast(id: PodcastId): Flow<Podcast> {
        return podcasts.map {
            it.first { podcast ->
                podcast.id == id
            }
        }
    }
}
