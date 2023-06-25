package com.lxn.gdghanoicheckin.di

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import com.lxn.gdghanoicheckin.repository.CheckInRepository
import com.lxn.gdghanoicheckin.repository.CheckInRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Namlxcntt
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSmSRepository(
        apiService: ApiService,
    ): CheckInRepository {
        return CheckInRepositoryImpl(
            apiService = apiService,
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

}