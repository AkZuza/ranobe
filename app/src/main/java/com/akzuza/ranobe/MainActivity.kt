package com.akzuza.ranobe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import com.akzuza.ranobe.ui.home.RanobeHome
import com.akzuza.ranobe.ui.theme.RanobeTheme
import com.akzuza.ranobe.viewmodel.RanobeHomeViewModel
import com.diskcache.diskcache.DiskCache
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: RanobeHomeViewModel by viewModels()
    private lateinit var diskCache: DiskCache
    private lateinit var imageDiskCache: ImageDiskCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dir = this.cacheDir
        diskCache = DiskCache(
            folder = dir,
            maxSize = 1024 * 1024 * 100,
            appVersion = 1,
            cleanupPercentage = 0.7,
        )

        imageDiskCache = ImageDiskCache(diskCache)

        enableEdgeToEdge()
        setContent {
            RanobeTheme {
                val ranobeHomeState by homeViewModel.uiState.collectAsStateWithLifecycle()
                RanobeHome(ranobeHomeState, homeViewModel::onEvent, imageDiskCache = imageDiskCache)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        homeViewModel.onEvent(RanobeHomeEvent.RefreshRanobes)
    }
}
