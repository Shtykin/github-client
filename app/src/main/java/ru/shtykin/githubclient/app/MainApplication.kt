package ru.shtykin.githubclient.app

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin
import ru.shtykin.githubclient.di.appModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}