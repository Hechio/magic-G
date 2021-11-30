package com.stevehechio.apps.magictheg.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stevehechio.apps.magictheg.data.local.convertors.ColorListTypeConvertor
import com.stevehechio.apps.magictheg.data.local.dao.CardsDao
import com.stevehechio.apps.magictheg.data.local.dao.CardsRemoteKeysDao
import com.stevehechio.apps.magictheg.data.local.dao.RemoteKeysDao
import com.stevehechio.apps.magictheg.data.local.dao.SetsDao
import com.stevehechio.apps.magictheg.data.local.entities.*
import com.stevehechio.apps.magictheg.utils.AppConstants

/**
 * Created by stevehechio on 11/27/21
 */

@Database(entities = [CardsEntity::class,SetsEntity::class,RemoteKeys::class,
    CardsRemoteKeys::class, CardsBoosterEntity::class],
    version = AppConstants.DB_VERSION,exportSchema = false)
@TypeConverters(ColorListTypeConvertor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cardsDao(): CardsDao
    abstract fun setsDao(): SetsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun cardsRemoteKeysDao(): CardsRemoteKeysDao

    //Room should only be initiated once, marked volatile to be thread safe.
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                AppConstants.DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }
}