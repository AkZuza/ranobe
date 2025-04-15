package com.akzuza.ranobe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RanobeDao {

    @Query("SELECT * FROM ranobes")
    fun getAll(): Flow<List<Ranobe>>

    @Query("SELECT * FROM ranobes WHERE id = :id")
    suspend fun getOne(id: Int) : Ranobe?

    @Query("SELECT * FROM ranobes WHERE title = :title")
    suspend fun getOne(title: String): Ranobe?

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertRanobe(ranobe: Ranobe)

    @Update
    suspend fun updateRanobe(ranobe: Ranobe)

    @Query("DELETE FROM ranobes WHERE id = :id")
    suspend fun deleteOne(id: Int)

    @Insert
    suspend fun addOne(ranobe: Ranobe)

}