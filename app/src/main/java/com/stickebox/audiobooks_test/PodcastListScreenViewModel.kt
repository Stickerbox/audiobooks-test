package com.stickebox.audiobooks_test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Failure
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Success
import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PodcastListScreenViewModel(
    private val loadPodcastsUseCase: LoadPodcastsUseCase,
) : ViewModel() {

    private var currentPage = 0

    private val _podcastUiState: MutableStateFlow<PodcastListUiState> =
        MutableStateFlow(PodcastListUiState.createInitialState())
    val podcastUiState = _podcastUiState.asStateFlow()

    init {
        onLoadPodcasts()
    }

    fun onFavouritePodcast(position: Int) {
        _podcastUiState.update { state ->
            val currentList = state.podcasts.toMutableList()
            currentList[position] =
                currentList[position].copy(isFavourite = !currentList[position].isFavourite)
            state.copy(
                podcasts = currentList
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
                        val currentList = state.podcasts.toMutableList()
                        currentList.addAll(podcastResult.result)
                        state.copy(
                            podcasts = currentList,
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