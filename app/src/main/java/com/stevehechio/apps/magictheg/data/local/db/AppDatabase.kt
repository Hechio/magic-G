package com.stevehechio.apps.magictheg.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stevehechio.apps.magictheg.data.local.convertors.ColorListTypeConvertor
import com.stevehechio.apps.magictheg.data.local.dao.CardsDao
import com.stevehechio.apps.magictheg.data.local.dao.RemoteKeysDao
import com.stevehechio.apps.magictheg.data.local.dao.SetsDao
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.local.entities.RemoteKeys
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import com.stevehechio.apps.magictheg.utils.AppConstants

/**
 * Created by stevehechio on 11/27/21
 */

@Database(entities = [CardsEntity::class,SetsEntity::class,RemoteKeys::class],
    version = AppConstants.DB_VERSION,exportSchema = false)
@TypeConverters(ColorListTypeConvertor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cardsDao(): CardsDao
    abstract fun setsDao(): SetsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}