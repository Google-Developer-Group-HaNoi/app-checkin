package com.lxn.gdghanoicheckin.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lxn.gdghanoicheckin.constant.Constant
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Namlxcntt
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithModifiers().create()
    }

    @Provides
    @Singleton
    fun providerOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
            level = HttpLoggingInterceptor.Level.BODY
        }
        client.connectTimeout(Constant.LOADER_EXPIRED_TIME.toLong(), TimeUnit.SECONDS)
        client.readTimeout(Constant.LOADER_EXPIRED_TIME.toLong(), TimeUnit.SECONDS)
        client.writeTimeout(Constant.LOADER_EXPIRED_TIME.toLong(), TimeUnit.SECONDS)
        client.addInterceptor { chain ->
            val original = chain.request()
            val request: Request
            val builder: Request.Builder = original.newBuilder()
                .method(original.method(), original.body())
            builder.header("token_dev","MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgHgsg6vJvRvBxoxvaURjikPbyqCm===" )
            request = builder.build()
            return@addInterceptor chain.proceed(request)
        }
        client.addInterceptor(loggingInterceptor)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideSmsRetrofit(retrofit: Retrofit.Builder): ApiService {
        return retrofit.build().create(ApiService::class.java)
    }
}