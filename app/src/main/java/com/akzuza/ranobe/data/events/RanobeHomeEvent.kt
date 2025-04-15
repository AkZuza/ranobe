package com.akzuza.ranobe.data.events

import android.net.Uri
import com.akzuza.ranobe.data.Ranobe

sealed interface RanobeHomeEvent {
    data class OpenRanobe(val id: Int): RanobeHomeEvent
    data class DeleteRanobe(val id: Int): RanobeHomeEvent
    data class UpdateRanobe(val id: Int, val ranobe: Ranobe): RanobeHomeEvent
    data class AddRanobeFromUri(val uri: Uri): RanobeHomeEvent

    data class UpdateRanobeSearchFilter(val filter: String): RanobeHomeEvent

    object RefreshRanobes: RanobeHomeEvent
    object StopRefreshRanobes: RanobeHomeEvent

    data class ShowModelSheet(val ranobe: Ranobe): RanobeHomeEvent
    object HideModalSheet: RanobeHomeEvent
}