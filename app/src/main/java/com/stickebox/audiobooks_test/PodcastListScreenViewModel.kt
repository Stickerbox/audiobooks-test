package com.stickebox.audiobooks_test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Failure
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Success
import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PodcastListScreenViewModel(
    private val loadPodcastsUseCase: LoadPodcastsUseCase,
    private val database: Database
) : ViewModel() {

    private var currentPage = 0

//    private val podcasts = mutableListOf<Podcast>()
    private val _podcastUiState: MutableStateFlow<PodcastListUiState> =
        MutableStateFlow(PodcastListUiState.createInitialState())
    val podcastUiState = _podcastUiState.asStateFlow()

    init {
        onLoadPodcasts()
    }

    suspend fun fetchPodcastById(id: String): Flow<Podcast> {
        return database.fetchPodcast(id)
//        return podcasts.firstOrNull { it.id == id }
    }

    fun onFavouritePodcast(podcast: Podcast) {
        _podcastUiState.update { state ->
            val position = podcasts.indexOf(podcast)
            podcasts[position] =
                podcasts[position].copy(isFavourite = !podcasts[position].isFavourite)
            state.copy(
                podcasts = podcasts
            )
        }
    }

    fun onLoadPodcasts() {
        _podcastUiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (val podcastResult = loadPodcastsUseCase.execute(PodcastPaginationState(page = currentPage + 1))) {
                is Success -> {
                    // On success, update the current page. Done here so that if there's a failure,
                    // we can maintain the previous state
                    currentPage += 1
                    _podcastUiState.update { state ->
                        podcasts.addAll(podcastResult.result)
                        state.copy(
                            podcasts = podcasts,
                            isLoading = false
                        )
                    }
                }

                is Failure -> {
                    _podcastUiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    data class PodcastListUiState(
        val podcasts: List<Podcast>,
        val isLoading: Boolean
    ) {
        companion object {
            fun createInitialState(): PodcastListUiState {
                return PodcastListUiState(
                    podcasts = emptyList(),
                    isLoading = false
                )
            }
        }
    }
}
