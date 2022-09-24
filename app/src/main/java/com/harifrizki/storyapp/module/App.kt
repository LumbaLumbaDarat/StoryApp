package com.harifrizki.storyapp.module

import androidx.multidex.MultiDexApplication
import com.harifrizki.storyapp.module.authentication.registration.RegistrationViewModel
import com.harifrizki.storyapp.utils.Injection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : MultiDexApplication() {
    @OptIn(KoinReflectAPI::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule
                )
            )
        }
    }

    @KoinReflectAPI
    @ExperimentalCoroutinesApi
    @FlowPreview
    val viewModelModule = module {
        viewModel { _ -> RegistrationViewModel(storyRepository = Injection.provideRepository()) }
    }
}