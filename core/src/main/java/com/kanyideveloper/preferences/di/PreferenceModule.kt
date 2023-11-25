package com.kanyideveloper.preferences.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kanyideveloper.core.util.Constants
import com.kanyideveloper.preferences.data.MealtimeSettingsImpl
import com.kanyideveloper.preferences.domain.MealtimeSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Provides
    @Singleton
    fun provideDatastorePreferences(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.MEALTIME_PREFERENCES)
            }
        )

    @Provides
    @Singleton
    fun provideMealtimeSettings(dataStore: DataStore<Preferences>): MealtimeSettings =
        MealtimeSettingsImpl(dataStore)
}