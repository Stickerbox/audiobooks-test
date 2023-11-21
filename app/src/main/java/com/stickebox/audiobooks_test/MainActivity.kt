package com.stickebox.audiobooks_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stickebox.audiobooks_test.ui.theme.AudiobookstestTheme
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            AudiobookstestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        NavHost(
                            navController = navController,
                            startDestination = Screens.List.routeName
                        ) {
                            composable(Screens.List.routeName) {
                                val viewModel = koinViewModel<PodcastListScreenViewModel>()
                                val uiState by viewModel.podcastUiState.collectAsState()

                                PodcastListScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    uiState = uiState,
                                    onEndOfList = {
                                        viewModel.onLoadPodcasts()
                                    },
                                    onPodcastClicked = { podcastId ->
                                        navController.navigate("/detail/$podcastId")
                                    }
                                )
                            }

                            composable(
                                Screens.Detail.routeName,
                                arguments = listOf(navArgument(Screens.Detail.podcastId) {
                                    type = NavType.StringType
                                })
                            ) { backstack ->
                                val podcastId =
                                    backstack.arguments?.getString(Screens.Detail.podcastId)
                                val viewModel = getViewModel<PodcastDetailViewModel>(parameters = { parametersOf(podcastId) })
                                val uiState by viewModel.podcastDetailUiState.collectAsState()
                                PodcastDetailScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    onFavouritePodcast = { viewModel.onFavouritePodcast(it) },
                                    uiState = uiState
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Screens(val routeName: String) {
    data object List : Screens("/list")
    data object Detail : Screens("/detail/{podcastId}") {
        val podcastId = "podcastId"
    }
}
