package com.akzuza.ranobe

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Room
import com.akzuza.ranobe.data.RanobeDao
import com.akzuza.ranobe.data.RanobeDatabase
import com.akzuza.ranobe.data.repositories.LocalRanobeRepository
import com.akzuza.ranobe.data.repositories.RanobeRepository
import com.akzuza.ranobe.data.repositories.RealRanobeRepository
import com.diskcache.diskcache.DiskCache
import com.diskcache.diskcache.wrapper.AndroidDiskCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RanobeModule {

    @Provides
    @Singleton
    fun provideRanobeDatabase(@ApplicationContext context: Context) : RanobeDatabase {
        return Room.databaseBuilder(context, RanobeDatabase::class.java, "ranobe-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAndroidDiskCacke(): AndroidDiskCache {
        return AndroidDiskCache.Builder
            .folder(java.io.File("thumbnails"))
            .appVersion(1)
            .maxSize(1024 * 1024 * 50)
            .dispatcher(Dispatchers.IO)
            .build()
    }

    @Provides
    @Singleton
    fun provideDiskCache(@ApplicationContext context: Context) : DiskCache {
        val dir = context.cacheDir
        return DiskCache(
            folder = dir,
            maxSize = 1024 * 1024 * 100,
            appVersion = 1,
            cleanupPercentage = 0.7,
        )
    }

    @Provides
    @Singleton
    fun provideImageDiskCache(diskCache: DiskCache) : ImageDiskCache = ImageDiskCache(diskCache)

    @Provides
    @Singleton
    fun provideRanobeDao(db: RanobeDatabase) = db.ranobeDao()

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context) = context.contentResolver

    @Provides
    @Singleton
    fun provideRanobeRepository(dao: RanobeDao) : RanobeRepository = RealRanobeRepository(dao)

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context) = context
}