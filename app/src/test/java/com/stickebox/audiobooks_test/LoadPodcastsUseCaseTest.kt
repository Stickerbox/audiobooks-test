package com.stickebox.audiobooks_test

import androidx.compose.ui.text.AnnotatedString
import com.stickebox.audiobooks_test.list.PodcastPaginationState
import com.stickebox.audiobooks_test.models.Podcast
import com.stickebox.audiobooks_test.persistence.Database
import com.stickebox.audiobooks_test.persistence.PodcastRepository
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase.LoadPodcastsResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoadPodcastsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var podcastRepository: PodcastRepository

    @MockK
    lateinit var database: Database

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when loadNextPage returns success, return Success`() = runBlocking {
        coEvery { podcastRepository.loadNextPage(any()) } returns Result.success(
            listOf(
                Podcast(
                    id = "",
                    name = "",
                    description = AnnotatedString(text = ""),
                    imageUrl = "",
                    isFavourite = true,
                    publisher = "",
                    thumbnailImageUrl = ""
                )
            )
        )

        coEvery { database.save(any()) } returns Unit

        val useCase = LoadPodcastsUseCase(podcastRepository, database)
        val result = useCase.execute(PodcastPaginationState(0))
        Assert.assertTrue(result is LoadPodcastsResult.Success)
    }

    @Test
    fun `when loadNextPage returns failure, return Failure`() = runBlocking {
        coEvery { podcastRepository.loadNextPage(any()) } returns Result.failure(Throwable("Failure"))

        coEvery { database.save(any()) } returns Unit

        val useCase = LoadPodcastsUseCase(podcastRepository, database)
        val result = useCase.execute(PodcastPaginationState(0))
        Assert.assertTrue(result is LoadPodcastsResult.Failure)
    }
}
