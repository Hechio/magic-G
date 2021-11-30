package com.stevehechio.apps.magictheg.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.stevehechio.apps.magictheg.data.local.dao.CardsDao
import com.stevehechio.apps.magictheg.data.local.dao.SetsDao
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by stevehechio on 11/30/21
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class AppDatabaseTest {
    private  lateinit var database: AppDatabase
    private lateinit var setsDao: SetsDao
    private lateinit var cardsDao: CardsDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        setsDao = database.setsDao()
        cardsDao = database.cardsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun testInsertAndGetCard(){
        val entities = ArrayList<CardsBoosterEntity>()
        val cardsBoosterEntity = CardsBoosterEntity("56999hudhjj98944", "Elven Riders",
            null,null,null,"4ED",null,null,null,null)
        entities.add(cardsBoosterEntity)
        cardsDao.insertAllBooster(entities)
        val resEntity = cardsDao.getBoosterCards("4ED").blockingFirst()
        assertEquals(cardsBoosterEntity.id, resEntity[0].id)
    }

}