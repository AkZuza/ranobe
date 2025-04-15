package com.akzuza.ranobe.data.repositories

import com.akzuza.ranobe.data.Ranobe
import kotlinx.coroutines.flow.Flow

interface RanobeRepository {

    fun getRanobesFlow() : Flow<List<Ranobe>>

    suspend fun getRanobe(id: Int) : Ranobe?

    suspend fun updateRanobe(id: Int, ranobe: Ranobe)

    suspend fun deleteRanobe(id: Int)

    suspend fun addRanobe(ranobe: Ranobe)

    suspend fun updateRanobe(ranobe: Ranobe)
}