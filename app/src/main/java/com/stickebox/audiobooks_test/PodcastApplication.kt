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

private const val BASE_URL = "https://listen-api-test.listennotes.com/api/v2/"

private val networkModule = module {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    single { retrofit.create(PodcastApiService::class.java) }

    single<Database> { InMemoryDatabase() }
    single { PodcastRepository(get()) }
}

private val useCaseModule = module {
    factory { LoadPodcastsUseCase(get(), get()) }
    factory { FavouritePodcastUseCase(get()) }
    factory { GetPodcastUseCase(get()) }
    factory { GetAllPodcastsUseCase(get()) }
}

private val viewModelModule = module {
    viewModel { PodcastListScreenViewModel(get(), get()) }
    viewModel { PodcastDetailViewModel(get(), get(), get()) }
}

class PodcastApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PodcastApplication)
            modules(networkModule, viewModelModule, useCaseModule)
        }
    }
}
