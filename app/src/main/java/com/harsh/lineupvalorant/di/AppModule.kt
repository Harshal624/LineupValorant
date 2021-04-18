package com.harsh.lineupvalorant.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.harsh.lineupvalorant.api.DailyMotionApi
import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.data.cache.VideoDatabase
import com.harsh.lineupvalorant.di.scope.ApplicationScope
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
/*
 This annotation tells Hilt that the dependencies provided via this module shall stay
 alive as long as application is running.
 */
@Module
/*
This annotation tells Hilt that this class contributes to dependency injection object graph.
 */
object AppModule {


    @Provides
    @Singleton
    fun provideoDatabase(appContext: Application): VideoDatabase = Room.databaseBuilder(
        appContext,
        VideoDatabase::class.java,
        "video_database"
    ).build()

    @Provides
            /*
            This annotation marks the method provideVideoDatabase as a provider of VideoDao.
            No need to add @Singleton annotation since it's singleton by default
            This is how room works :o
             */
    fun provideVideoDao(videoDatabase: VideoDatabase): VideoDao = videoDatabase.videoDao()


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
    //SupervisorJob says: When any of the coroutine child job fails, continue the operation


    @Provides
    fun provideBaseUrl() = DailyMotionApi.DAILYMOTION_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideDailyMotionApiService(retrofit: Retrofit) =
        retrofit.create(DailyMotionApi::class.java)

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideDataStore(application: Application): CoreDataStore =
        CoreDataStore(application)

}