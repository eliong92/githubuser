package com.eliong92.githubuser.di

import android.app.Application
import com.eliong92.githubuser.GithubUserApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBuilder::class,
    NetworkModule::class,
    UseCaseModule::class,
    RepositoryModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: GithubUserApp)
}