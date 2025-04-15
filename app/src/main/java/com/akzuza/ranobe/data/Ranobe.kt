package com.akzuza.ranobe.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ranobes")
data class Ranobe(
    val title: String,
    val path: Uri,
    @PrimaryKey(autoGenerate = true) val id: Int,
    val progress: Int,
    val totalPages: Int
)
