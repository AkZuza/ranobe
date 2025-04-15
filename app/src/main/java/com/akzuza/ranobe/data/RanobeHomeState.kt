package com.akzuza.ranobe.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class RanobeHomeState (
    val ranobes: List<Ranobe> = emptyList(),
    val openingRanobe: String = "",
    val showModalSheet: Boolean = false,
    val selectedRanobe: Ranobe? = null,
    val filter: String = ""
)