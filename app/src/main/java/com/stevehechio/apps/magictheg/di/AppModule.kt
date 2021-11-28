package com.stevehechio.apps.magictheg.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.remote.api.CardsApiService
import com.stevehechio.apps.magictheg.data.remote.api.SetsApiService
import com.stevehechio.apps.magictheg.data.repository.SetsRepository
import com.stevehechio.apps.magictheg.ui.viewmodels.SetsViewModel
import com.stevehechio.apps.magictheg.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by stevehechio on 11/27/21
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Here we return the Gson object
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
    /**
     * Here we return the Retrofit object
     */
    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    /**
     * Here we return the OKhttp object
     */
    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .baseUrl(AppConstants.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideSetApiService(retrofit: Retrofit): SetsApiService {
        return retrofit.create(SetsApiService::class.java)
    }

    fun provideCardApiService(retrofit: Retrofit):CardsApiService{
        return  retrofit.create(CardsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, AppConstants.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSetsRepository(service: SetsApiService, appDatabase: AppDatabase): SetsRepository {
        return SetsRepository(service,appDatabase)
    }
    @Provides
    @Singleton
    fun  provideSetsViewModel(setsRepository: SetsRepository): SetsViewModel {
        return SetsViewModel(setsRepository)
    }
}