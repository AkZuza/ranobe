package com.akzuza.ranobe.data

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

data class RanobeReaderState(
    val currentPage: Int = 1,
    val totalPages: Int = -1,
    val initialPage: Int = 0,
    val loaded: Boolean = false,
    val ranobe: Ranobe? = null,
    val showHeader: Boolean = true
)
