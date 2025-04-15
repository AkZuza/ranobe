package com.akzuza.ranobe.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RanobeSearchBar(onEvent: (String) -> Unit) {
    var searchFilter by remember { mutableStateOf("") }
    val pillShape = RoundedCornerShape(100.dp)

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchFilter,
        onValueChange = { searchFilter = it; onEvent(searchFilter) },
        placeholder = { Text("Search") },
        singleLine = true,
        shape = pillShape,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        prefix = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },

        suffix = {
            if (searchFilter.isNotEmpty()) {
                IconButton(
                    onClick = { searchFilter = ""; onEvent(searchFilter) },
                    modifier = Modifier.size(24.dp, 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = null,
                    )
                }
            }
        }
    )
}