package ru.shtykin.githubclient.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.shtykin.githubclient.data.db.GithubClientDataBase
import ru.shtykin.githubclient.data.mapper.Mapper
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
    single {
        Room.databaseBuilder(
            get(),
            GithubClientDataBase::class.java,
            "db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { Mapper() }
    single<Repository> {
        RepositoryImpl(
            get(),
            get(),
            get(),
        )
    }
    viewModel { MainViewModel(get()) }
}