package com.stickebox.audiobooks_test

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Failure
import com.stickebox.audiobooks_test.LoadPodcastsUseCase.LoadPodcastsResult.Success
import com.stickebox.audiobooks_test.models.Podcast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PodcastDetailViewModel(
    private val podcastId: String,
    private val database: Database
) : ViewModel() {

    val podcastDetailUiState = database.fetchPodcast(podcastId)
        .distinctUntilChanged()
        .map {  podcast ->
            PodcastDetailUiState(
                title = podcast.name,
                publisher = podcast.publisher,
                imageUrl = podcast.imageUrl,
                isFavourite = podcast.isFavourite,
                description = podcast.description
            )
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PodcastDetailUiState.createInitialState()
        )

    fun onFavouritePodcast(isFavourite: Boolean) {
        viewModelScope.launch {
            database.updateFavouriteState(podcastId = podcastId, isFavourite = isFavourite)
        }
    }

    data class PodcastDetailUiState(
        val title: String,
        val publisher: String,
        val imageUrl: String,
        val isFavourite: Boolean,
        val description: AnnotatedString,
    ) {
        companion object {
            fun createInitialState(): PodcastDetailUiState {
                return PodcastDetailUiState(
                    "",
                    "",
                    "",
                    false,
                    AnnotatedString("")
                )
            }
        }
    }
}

@KoinViewModel
class PodcastListScreenViewModel(
    private val loadPodcastsUseCase: LoadPodcastsUseCase,
    private val database: Database
) : ViewModel() {

    private var currentPage = 0

    private val _podcastUiState: MutableStateFlow<PodcastListUiState> =
        MutableStateFlow(PodcastListUiState.createInitialState())
    val podcastUiState = _podcastUiState.asStateFlow()

    init {
        viewModelScope.launch {
            database.podcasts
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
            when (val podcastResult =
                loadPodcastsUseCase.execute(PodcastPaginationState(page = currentPage + 1))) {
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
