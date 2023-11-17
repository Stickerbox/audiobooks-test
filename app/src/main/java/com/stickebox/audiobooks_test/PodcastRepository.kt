package com.stickebox.audiobooks_test

import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.network.PodcastApiService
import com.stickebox.audiobooks_test.network.models.toDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@JvmInline
value class PodcastPaginationState(val page: Int)

class PodcastRepository(
    private val apiService: PodcastApiService,
    private val database: Database
) {

    suspend fun loadNextPage(nextState: PodcastPaginationState): Result<Unit> {
        val podcastsResponse = apiService.getBestPodcasts(nextState.page)
        if (!podcastsResponse.isSuccessful) {
            return Result.failure(Throwable("Unable to load podcasts with state $nextState"))
        }

        val podcasts = podcastsResponse.body()?.podcasts?.map { it.toDomainModel() }
            ?: return Result.failure(Throwable("Unable to load podcasts with state $nextState"))

        database.save(podcasts)
        return Result.success(Unit)
    }

    suspend fun switchPodcastFavouriteState(podcast: Podcast) {
        database.updateFavouriteState(podcast.id, isFavourite = !podcast.isFavourite)
    }
}

/**
 * An abstract definition of a database
 */
interface Database {
    /**
     * A [StateFlow] of the current [List] of [Podcast] that has been collected and saved so far
     */
    val podcasts: StateFlow<List<Podcast>>

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

/**
 * A local in-memory implementation of [Database].
 */
class InMemoryDatabase : Database {

    private val _podcasts: MutableStateFlow<List<Podcast>> = MutableStateFlow(emptyList())
    override val podcasts: StateFlow<List<Podcast>> = _podcasts.asStateFlow()

    /**
     * Saves the podcasts to a [StateFlow], mainly so that the [Podcast::isFavourite] state
     * can be flipped later on
     */
    override suspend fun save(podcasts: List<Podcast>) {
        val mutableList = _podcasts.value.toMutableList()
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
        val mutableList = _podcasts.value.toMutableList()
        val podcastToFavourite = mutableList.firstOrNull { it.id == podcastId } ?: return
        val updatedPodcast = podcastToFavourite.copy(isFavourite = isFavourite)
        val currentPosition = mutableList.indexOf(podcastToFavourite)
        mutableList.remove(podcastToFavourite)
        mutableList.add(currentPosition, updatedPodcast)

        _podcasts.emit(mutableList.toList())
    }
}
