package com.akzuza.ranobe.viewmodel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akzuza.ranobe.ImageDiskCache
import com.akzuza.ranobe.RanobeReader
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.RanobeHomeState
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import com.akzuza.ranobe.data.repositories.RanobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RanobeHomeViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val ranobeRepository: RanobeRepository,
    private val imageDiskCache: ImageDiskCache
)
    : ViewModel() {

    private val _uiState = MutableStateFlow(RanobeHomeState())
    val uiState: StateFlow<RanobeHomeState> = _uiState.asStateFlow()


    private suspend fun refreshRanobes() {
        ranobeRepository.getRanobesFlow().collect { ranobes ->
            _uiState.update { it.copy(ranobes = ranobes) }
        }
    }

    private fun deleteRanobe(id: String) {

    }

    private fun openRanobe(id: String) {

    }

    init {
        viewModelScope.launch {
            ranobeRepository.getRanobesFlow().collect { ranobes ->
                _uiState.update {
                    it.copy(ranobes = ranobes)
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun addRanobe(uri: Uri) {

        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val cursor = contentResolver.query(
            uri, null, null, null, null, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.d("Ranobe", "addRanobe: DISPLAY_NAME: $displayName")

                val ranobe = Ranobe(
                    title = displayName,
                    path =  uri,
                    id = 0,
                    progress = 0,
                    totalPages = 0
                )

                viewModelScope.launch {
                    ranobeRepository.addRanobe(ranobe)
                }

                viewModelScope.launch (
                   Dispatchers.IO
                ) {


                    val newtitle = ranobe.title.replace(' ', '_').removeSuffix(".pdf")
                    if(imageDiskCache.contains(newtitle) == true) {
                        Log.d("RanobeHome", "addRanobe: Already exits for ${ranobe.title}")
                        return@launch
                    }

                    val fd: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
                    if(fd == null) return@launch
                    val bitmap = createBitmap(2100/7, 2970/7)
                    val renderer = PdfRenderer(fd)
                    val page = renderer.openPage(1)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    renderer.close()
                    fd.close()

                    Log.d("RanobeHomeViewModel", "addRanobe: storing image")
                    imageDiskCache.put(newtitle, bitmap)
                    bitmap.recycle()
                    Log.d("RanobeHomeViewModel", "addRanobe: stored image")
                }
            }
        }
    }

    fun onEvent(event: RanobeHomeEvent) {

        when(event) {
            is RanobeHomeEvent.OpenRanobe -> {

            }

            is RanobeHomeEvent.DeleteRanobe -> {
                val id = event.id
                viewModelScope.launch {
                    ranobeRepository.deleteRanobe(id)
                }
            }

            RanobeHomeEvent.RefreshRanobes -> {
                viewModelScope.launch {
                    refreshRanobes()
                }
            }

            RanobeHomeEvent.StopRefreshRanobes -> {

            }

            is RanobeHomeEvent.UpdateRanobe -> {
                viewModelScope.launch {
                    ranobeRepository.updateRanobe(event.ranobe)
                }
            }

            is RanobeHomeEvent.UpdateRanobeSearchFilter -> {
                val filter = event.filter
                _uiState.update { it.copy(filter = filter) }
            }

            is RanobeHomeEvent.AddRanobeFromUri -> {
                val uri = event.uri
                addRanobe(uri)
                viewModelScope.launch {
                    refreshRanobes()
                }
            }

            RanobeHomeEvent.HideModalSheet -> {
                _uiState.update {
                    it.copy(
                        selectedRanobe = null,
                        showModalSheet = false
                    )
                }
            }
            is RanobeHomeEvent.ShowModelSheet -> {
                val ranobe = event.ranobe
                _uiState.update {
                    it.copy(
                        selectedRanobe = ranobe,
                        showModalSheet = true
                    )
                }
            }
        }
    }
}