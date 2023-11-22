package com.stickebox.audiobooks_test.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase.LoadPodcastsResult.Failure
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase.LoadPodcastsResult.Success
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.usecase.GetAllPodcastsUseCase
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PodcastListScreenViewModel(
    private val loadPodcastsUseCase: LoadPodcastsUseCase,
    private val getAllPodcastsUseCase: GetAllPodcastsUseCase,
) : ViewModel() {

    private var currentPage = 0

    private val _podcastUiState: MutableStateFlow<PodcastListUiState> =
        MutableStateFlow(PodcastListUiState.createInitialState())
    val podcastUiState = _podcastUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllPodcastsUseCase.execute()
                .collectLatest { podcasts ->
                    _podcastUiState.update {
                        it.copy(podcasts = podcasts)
                    }
                }
        }
        onLoadPodcasts()
    }

    fun onLoadPodcasts() {
        _podcastUiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (loadPodcastsUseCase.execute(PodcastPaginationState(page = currentPage + 1))) {
                is Success -> {
                    // On success, update the current page. Done here so that if there's a failure,
                    // we can maintain the previous state
                    currentPage += 1
                    _podcastUiState.update { state ->
                        state.copy(isLoading = false)
                    }
                }

                is Failure -> {
                    _podcastUiState.update { state ->
                        state.copy(isLoading = false)
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
