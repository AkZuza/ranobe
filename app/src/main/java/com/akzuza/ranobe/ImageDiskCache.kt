package com.akzuza.ranobe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.diskcache.diskcache.DiskCache
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageDiskCache @Inject constructor(private val diskCache: DiskCache) {

    fun get(key: String): Bitmap? {
        var bitmap: Bitmap? = null
        diskCache.get(key)?.let { snapshot ->
            bitmap = BitmapFactory.decodeStream(FileInputStream(snapshot.file()))
            snapshot.close()
        }
        return bitmap
    }

    fun contains(key: String): Boolean {
        return diskCache.get(key) != null
    }

    fun put(key: String, bitmap: Bitmap) {
        diskCache.edit(key)?.let { editor ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(editor.file()))
            editor.commit()
        }
    }

    fun clear() {
        diskCache.evictAll()
    }

}