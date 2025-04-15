package com.akzuza.ranobe.data

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class RanobeTypeConverter {
    @TypeConverter
    fun UriToString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun StringToUri(str: String): Uri {
        return str.toUri()
    }
}