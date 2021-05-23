package com.example.accesstest

import android.app.Application
import android.content.Context
import com.example.accesstest.ui.MainViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
    }

    private val repoModule = module {
//        single { DataRepository(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    repoModule
                )
            )
        }
    }
}