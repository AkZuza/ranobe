package com.akzuza.ranobe.ui.reader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

@Composable
fun RanobeReaderProgressHeader(title: String, progress: Int, total: Int) {
    val fraction = (progress.toFloat() / total)

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(12.dp)) ; Text(title)

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = {
                fraction
            }
        )
    }
}
