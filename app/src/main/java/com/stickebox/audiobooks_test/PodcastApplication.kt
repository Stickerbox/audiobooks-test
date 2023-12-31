package com.stickebox.audiobooks_test

import android.app.Application
import com.stickebox.audiobooks_test.detail.PodcastDetailViewModel
import com.stickebox.audiobooks_test.list.PodcastListScreenViewModel
import com.stickebox.audiobooks_test.network.PodcastApiService
import com.stickebox.audiobooks_test.persistence.Database
import com.stickebox.audiobooks_test.persistence.InMemoryDatabase
import com.stickebox.audiobooks_test.persistence.PodcastRepository
import com.stickebox.audiobooks_test.usecase.FavouritePodcastUseCase
import com.stickebox.audiobooks_test.usecase.GetAllPodcastsUseCase
import com.stickebox.audiobooks_test.usecase.GetPodcastUseCase
import com.stickebox.audiobooks_test.usecase.LoadPodcastsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PodcastApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PodcastApplication)
            modules(networkModule, viewModelModule, useCaseModule)
        }
    }
}
