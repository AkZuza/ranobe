package com.akzuza.ranobe

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.events.RanobeReaderEvent
import com.akzuza.ranobe.data.repositories.LocalRanobeRepository
import com.akzuza.ranobe.ui.reader.RanobeReader
import com.akzuza.ranobe.ui.theme.RanobeTheme
import com.akzuza.ranobe.viewmodel.RanobeHomeViewModel
import com.akzuza.ranobe.viewmodel.RanobeReaderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class RanobeReader : ComponentActivity() {

    private val readerViewModel: RanobeReaderViewModel by viewModels()

    override fun onStop() {
        super.onStop()
        val currentPage = readerViewModel.uiState.value.currentPage
        val totalPages = readerViewModel.uiState.value.totalPages
        val ranobe = readerViewModel.uiState.value.ranobe

        if (ranobe == null) {
            return
        }

        val newRanobe = Ranobe(
            title = ranobe.title,
            progress = currentPage,
            totalPages = totalPages,
            id = ranobe.id,
            path = ranobe.path
        )

        readerViewModel.onEvent(RanobeReaderEvent.CloseRanobe(
            newRanobe
        ))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("ranobe", 0)

        enableEdgeToEdge()
        setContent {
            RanobeTheme {
                val readerModel by readerViewModel.uiState.collectAsStateWithLifecycle()
                readerViewModel.onEvent(RanobeReaderEvent.OpenRanobe(id))
                RanobeReader(readerModel, readerViewModel::onEvent)
            }
        }
    }
}


