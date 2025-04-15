package com.akzuza.ranobe.ui.home

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import kotlinx.coroutines.launch
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlin.math.max

@Composable
fun RanobeCard(
    ranobe: Ranobe,
    openRanobe: () -> Unit,
    renderImage: @Composable (String) -> Unit,
    fd: ParcelFileDescriptor?,
    onEvent: (RanobeHomeEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val progress = ranobe.progress
    val total = max(1,ranobe.totalPages)
    val fraction: Float = progress.toFloat() / total

    var bitmapCache: MutableMap<String, Bitmap> by remember {
        mutableMapOf()
    }

    Card (
        modifier = Modifier
            .width(130.dp)
            .height(320.dp),
        onClick = {
            openRanobe()
        },
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                ranobe.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.width(110.dp)
            )

            IconButton (
                onClick = {
                    onEvent(
                        RanobeHomeEvent.ShowModelSheet(ranobe)
                    )
                }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = {
                fraction
            }
        )

        renderImage(ranobe.title)

//        scope.launch (Dispatchers.Default ) {
//            if(fd != null && !bitmapCache.containsKey(ranobe.title)) {
//                Log.d("RanobeCard", "RanobeCard: Rendering bitmap ${ranobe.title}")
//                bitmapCache[ranobe.title] = createBitmap(2100/7, 2970/7)
//                val renderer = PdfRenderer(fd)
//                val page = renderer.openPage(1)
//                bitmapCache[ranobe.title]?.let {
//                    page.render(it, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//                }
//                page.close()
//                renderer.close()
//            }
//
//            fd?.close()
//        }
//
//        val bitmap = bitmapCache.get(ranobe.title);
//        if(bitmap != null) {
//            Image(
//                modifier = Modifier.fillMaxSize(),
//                bitmap = bitmap.asImageBitmap(),
//                contentDescription = ""
//            )
//        }

    }

}

@Preview
@Composable
fun PreviewRanobeCard() {
    val title = "Random title"
    val progress = 28
    val total = 50
    val id = 0
    val path = "".toUri()

    val ranobe = Ranobe(
        id = id,
        title = title,
        progress = progress,
        totalPages = total,
        path = path
    )

}