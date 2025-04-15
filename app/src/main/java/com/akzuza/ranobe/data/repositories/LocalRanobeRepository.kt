package com.akzuza.ranobe.data.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.akzuza.ranobe.data.Ranobe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

var ranobesList = mutableListOf<Ranobe>()
var count = 0

class LocalRanobeRepository : RanobeRepository {

    val ranobesListFLow: Flow<List<Ranobe>> = flow {
        emit(ranobesList)
    }
    override fun getRanobesFlow(): Flow<List<Ranobe>> = ranobesListFLow

    override suspend fun getRanobe(id: Int): Ranobe? {
        // right now id is just the title
        val ranobe = ranobesList.find { it.id == (id) }
        return ranobe
    }

    override suspend fun updateRanobe(id: Int, ranobe: Ranobe) {
        val index = ranobesList.indexOfFirst { it.id == id }
        if (index == -1) return;

        ranobesList[index] = ranobe
        refreshRanobes()
    }

    override suspend fun deleteRanobe(id: Int) {
        val index = ranobesList.indexOfFirst { it.id == (id) }
        if (index == -1) return;

        ranobesList.removeAt(index)
        refreshRanobes()
    }

    // Simple as hell but works
    // with actual database we wont have to implement diffutil or any of that
    override suspend fun addRanobe(ranobe: Ranobe) {
        val fake = Ranobe(
            title = ranobe.title, id = count, progress = ranobe.progress, path = ranobe.path,
            totalPages = ranobe.totalPages)
        count = count + 1
        ranobesList.add(fake)

        val temp = ranobesList.toList()
        ranobesList.clear()
        ranobesList = mutableListOf()
        temp.forEach { ranobe -> ranobesList.add(ranobe) }
    }

    override suspend fun updateRanobe(ranobe: Ranobe) {
        TODO("Not yet implemented")
    }

    fun refreshRanobes() {
        val temp = ranobesList.toList()
        ranobesList.clear()
        ranobesList = mutableListOf()
        temp.forEach { ranobe -> ranobesList.add(ranobe) }
    }
}