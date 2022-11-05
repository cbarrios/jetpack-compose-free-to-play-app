package com.lalosapps.freetoplay.core.di

import android.content.Context
import com.lalosapps.freetoplay.FreeToPlayApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFreeToPlayApp(
        @ApplicationContext app: Context
    ): FreeToPlayApp {
        return app as FreeToPlayApp
    }
}