package com.kanyideveloper.mealtime.di

import android.content.Context
import androidx.room.Room
import com.kanyideveloper.mealtime.data.local.MealTimeDatabase
import com.kanyideveloper.mealtime.util.Constants.MEALTIME_DATABASE
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
    fun provideMealTimeDatabase(@ApplicationContext context: Context): MealTimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MealTimeDatabase::class.java,
            MEALTIME_DATABASE
        ).build()
    }
}