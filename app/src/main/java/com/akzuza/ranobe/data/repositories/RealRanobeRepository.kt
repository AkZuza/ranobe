package com.akzuza.ranobe.data.repositories

import android.util.Log
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.RanobeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class RealRanobeRepository @Inject constructor(
    private val dao : RanobeDao
) : RanobeRepository {

    override fun getRanobesFlow(): Flow<List<Ranobe>> {
        return dao.getAll()
    }

    override suspend fun getRanobe(id: Int): Ranobe? {
        return dao.getOne(id)
    }

    override suspend fun updateRanobe(id: Int, ranobe: Ranobe) {

    }

    override suspend fun deleteRanobe(id: Int) {
        dao.deleteOne(id)
    }

    override suspend fun addRanobe(ranobe: Ranobe) {
        if(dao.getOne(ranobe.title) != null) {
            Log.d("RealRanobeRepository", "addRanobe: Duplicate ranobe being added, exited")
            return
        }
        dao.addOne(ranobe)
    }

    override suspend fun updateRanobe(ranobe: Ranobe) {
        dao.updateRanobe(ranobe)
    }

}