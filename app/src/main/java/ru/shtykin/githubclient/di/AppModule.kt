package ru.shtykin.githubclient.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.shtykin.githubclient.presentation.MainViewModel

val appModule = module {

    viewModel { MainViewModel() }
}