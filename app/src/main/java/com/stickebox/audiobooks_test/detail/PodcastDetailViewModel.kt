package com.stickebox.audiobooks_test.detail

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.audiobooks_test.usecase.FavouritePodcastUseCase
import com.stickebox.audiobooks_test.usecase.GetPodcastUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PodcastDetailViewModel(
    private val podcastId: String,
    getPodcastUseCase: GetPodcastUseCase,
    private val favouritePodcastUseCase: FavouritePodcastUseCase
) : ViewModel() {

    val podcastDetailUiState = getPodcastUseCase.execute(podcastId)
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
            favouritePodcastUseCase.execute(podcastId = podcastId, isFavourite = isFavourite)
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
