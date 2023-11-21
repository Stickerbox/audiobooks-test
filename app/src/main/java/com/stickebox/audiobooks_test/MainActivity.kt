package com.stickebox.audiobooks_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.stickebox.audiobooks_test.ui.theme.AudiobookstestTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudiobookstestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = koinViewModel<PodcastListScreenViewModel>()
                    val uiState by viewModel.podcastUiState.collectAsState()

                    PodcastListScreen(
                        modifier = Modifier.fillMaxSize(),
                        uiState = uiState,
                        onEndOfList = {
                            viewModel.onLoadPodcasts()
                        }
                    )
                }
            }
        }
    }
}
