package com.eliong92.githubuser.di

import com.eliong92.githubuser.MainActivity
import com.eliong92.githubuser.viewmodel.MainViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainViewModelModule::class])
    abstract fun bindMainActivity(): MainActivity
}