package com.lalosapps.freetoplay.core.di

import android.content.Context
import com.lalosapps.freetoplay.FreeToPlayApp
import com.lalosapps.freetoplay.data.remote.api.GamesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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

    @Provides
    @Singleton
    fun provideGamesApi(): GamesApi {
        return Retrofit.Builder()
            .baseUrl(GamesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}