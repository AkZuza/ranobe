package com.akzuza.ranobe.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import javax.inject.Inject
import javax.inject.Singleton

@Database(
    entities = [Ranobe::class],
    version = 2
)
@TypeConverters(RanobeTypeConverter::class)
abstract class RanobeDatabase : RoomDatabase() {
    abstract fun ranobeDao() : RanobeDao
}