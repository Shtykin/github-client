package ru.shtykin.githubclient.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.shtykin.githubclient.data.network.ApiService
import ru.shtykin.githubclient.data.repository.RepositoryImpl
import ru.shtykin.githubclient.domain.Repository
import ru.shtykin.githubclient.presentation.MainViewModel

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single<Repository> {
        RepositoryImpl(
            get()
        )
    }
    viewModel { MainViewModel(get()) }
}