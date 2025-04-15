package com.akzuza.ranobe.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.akzuza.ranobe.ImageDiskCache
import com.akzuza.ranobe.data.RanobeHomeState
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import com.akzuza.ranobe.RanobeReader
import com.akzuza.ranobe.data.Ranobe
import com.frosch2010.fuzzywuzzy_kotlin.FuzzySearch
import com.frosch2010.fuzzywuzzy_kotlin.ToStringFunction
import com.frosch2010.fuzzywuzzy_kotlin.model.BoundExtractedResult
import com.frosch2010.fuzzywuzzy_kotlin.model.ExtractedResult
import kotlinx.coroutines.launch

@Composable
fun RanobeHome(
    homeState: RanobeHomeState,
    onEvent: (RanobeHomeEvent) -> Unit,
    imageDiskCache: ImageDiskCache? = null
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddRanobeFloatingButton {uri ->
                onEvent(RanobeHomeEvent.AddRanobeFromUri(uri))
            }
        },
        topBar = {
            // ScaffoldAppBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        if(homeState.showModalSheet && homeState.selectedRanobe != null) {
            RanobeModelSheet(
                homeState.selectedRanobe,
                onSnackBarCallback = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            it
                        )
                    }
                }
            ) {
                onEvent(it)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp)
        ) {
            RanobeSearchBar { filter -> onEvent(RanobeHomeEvent.UpdateRanobeSearchFilter(filter)) }

            Spacer(modifier = Modifier.height(12.dp))

            // Lazy Column for stuff
            LazyVerticalGrid (
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.FixedSize(180.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // perform fuzzy search
                //var matches: List<BoundExtractedResult<Ranobe>>? = null
                val filter = homeState.filter
                if (filter.isEmpty() == false) {

                }

                items (count = homeState.ranobes.size) {
                    val ranobe = homeState.ranobes[it]
//                    val fd = context.contentResolver.openFileDescriptor(ranobe.path, "r")
                    RanobeCard(
                        ranobe,
                        fd = null,
                        renderImage = { title ->
                            val newTitle = title.replace(' ', '_').removeSuffix(".pdf")
                            val bitmap = imageDiskCache?.get(newTitle)
                            val imageBitmap = bitmap?.asImageBitmap()

                            if(imageBitmap != null) {
                                Log.d("RanobeHome", "RanobeHome: Rendering image")
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    bitmap = imageBitmap,
                                    contentDescription = null
                                )
                            }
                        },
                        openRanobe = {
                            val intent = Intent(context, RanobeReader::class.java).apply {
                                putExtra("ranobe", ranobe.id)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        onEvent(it)
                    }
//                    fd?.close()
                }
            }
        }
    }

}

@Composable
fun AddRanobeFloatingButton(onEvent: (Uri) -> Unit) {
    var ranobeUri: Uri? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if ( result.resultCode == Activity.RESULT_OK) {
            ranobeUri = result.data?.data
        }
    }

    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        type = "application/pdf"
    }


    FloatingActionButton(
        onClick = {
            launcher.launch(intent)

        }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }

    ranobeUri?.let {
        Log.d("Ranobe", "AddRanobeFloatingButton: $it")
        onEvent(it)
    }

}
