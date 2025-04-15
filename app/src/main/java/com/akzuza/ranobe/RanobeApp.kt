package com.akzuza.ranobe

import androidx.compose.runtime.Composable
import com.akzuza.ranobe.data.RanobeHomeState
import com.akzuza.ranobe.data.events.RanobeHomeEvent
import com.akzuza.ranobe.ui.home.RanobeHome

@Composable
fun RanobeApp(
    ranobeHomeState: RanobeHomeState,
    ranobeHomeOnEvent: (RanobeHomeEvent) -> Unit
) {
    RanobeHome(ranobeHomeState, ranobeHomeOnEvent)
}