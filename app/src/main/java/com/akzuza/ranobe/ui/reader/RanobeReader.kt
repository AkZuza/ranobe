package com.akzuza.ranobe.ui.reader

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.acutecoder.pdf.PdfViewer
import com.acutecoder.pdfviewer.compose.CustomOnReadyCallback
import com.acutecoder.pdfviewer.compose.DefaultOnReadyCallback
import com.acutecoder.pdfviewer.compose.rememberPdfState
import com.acutecoder.pdfviewer.compose.ui.PdfViewer
import com.acutecoder.pdfviewer.compose.ui.PdfViewerContainer
import com.akzuza.ranobe.data.RanobeReaderState
import com.akzuza.ranobe.data.events.RanobeReaderEvent

@Composable
fun RanobeReader(readerState: RanobeReaderState, onEvent: (RanobeReaderEvent) -> Unit) {

    Scaffold (
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures() {
                val event = if (readerState.showHeader) RanobeReaderEvent.HideHeader
                else RanobeReaderEvent.ShowHeader
                onEvent(event)
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (readerState.showHeader) {
                RanobeReaderProgressHeader(
                    readerState.ranobe?.title ?: "",
                    readerState.currentPage,
                    readerState.totalPages
                )
            }

            if (readerState.ranobe != null) {

                val pdfState = rememberPdfState(readerState.ranobe.path.toString())
                pdfState.load(readerState.ranobe.path.toString())

                val initial = readerState.ranobe.progress
                val pageCount = pdfState.pagesCount
                val currentPage = pdfState.currentPage

                LaunchedEffect(pageCount) {
                    onEvent(RanobeReaderEvent.SetTotalPageCount(pageCount))

                    pdfState.pdfViewer?.run {
                        goToPage(initial)
                        onEvent(RanobeReaderEvent.SetInitialPage(initial))
                    }
                }

                LaunchedEffect(currentPage) {
                    onEvent(RanobeReaderEvent.SetCurrentPageNo(currentPage))
                }

                PdfViewerContainer(
                    pdfState = pdfState,
                    pdfViewer = {
                        PdfViewer(
                            modifier = Modifier.fillMaxSize().clickable {
                                val event: RanobeReaderEvent = if(readerState.showHeader)
                                    RanobeReaderEvent.HideHeader
                                else RanobeReaderEvent.ShowHeader
                                onEvent(event)
                            },
                        )
                    }
                )
            }
        }
    }

}