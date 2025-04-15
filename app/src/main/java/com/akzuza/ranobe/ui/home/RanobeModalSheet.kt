package com.akzuza.ranobe.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RanobeModelSheet(
    ranobe: Ranobe,
    onSnackBarCallback: (String) -> Unit,
    onEvent: (RanobeHomeEvent) -> Unit) {

    val completedPerc: Int = ((ranobe.progress.toFloat() / max(ranobe.totalPages, 1)) * 100f).toInt()

    ModalBottomSheet(
        onDismissRequest = {
            onEvent(RanobeHomeEvent.HideModalSheet)
        }
    ) {
        Column (
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                ranobe.title
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 3.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Text("Read ${ranobe.progress} page${if(ranobe.progress == 1) "" else "s"}")
                VerticalDivider(thickness = 3.dp)
                Text("${completedPerc}% Complete")
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 3.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                TextButton(
                    onClick = {
                        onEvent(RanobeHomeEvent.DeleteRanobe(ranobe.id))
                        onEvent(RanobeHomeEvent.HideModalSheet)
                        onSnackBarCallback("Deleted ${ranobe.title}")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ranobe",
                        tint = Color.Red
                    )

                    Text(
                        "Delete",
                        color = Color.Red,
                        fontSize = 18.sp
                    )
                }

                TextButton(
                    onClick = {
                        val updateRanobe = ranobe.copy(progress = 0)
                        onEvent(RanobeHomeEvent.UpdateRanobe(updateRanobe.id, updateRanobe))
                        onEvent(RanobeHomeEvent.HideModalSheet)
                        onSnackBarCallback("Reset progress to 0")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Read from start",
                    )

                    Text(
                        "Reset",
                        fontSize = 18.sp
                    )
                }

            }

            // Spacer(modifier = Modifier.height(12.dp))
        }
    }
}