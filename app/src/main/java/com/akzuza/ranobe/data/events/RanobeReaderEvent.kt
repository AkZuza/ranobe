package com.akzuza.ranobe.data.events

import com.akzuza.ranobe.data.Ranobe

sealed interface RanobeReaderEvent {
    data class OpenRanobe(val id: Int): RanobeReaderEvent
    data class CloseRanobe(val ranobe: Ranobe): RanobeReaderEvent

    data class SetCurrentPageNo(val pageNo: Int): RanobeReaderEvent
    data class SetTotalPageCount(val pageCount: Int): RanobeReaderEvent

    data class SetInitialPage(val page: Int): RanobeReaderEvent

    data class SetEssentialRanobeDetails(val currentPage: Int, val totalPageCount: Int)

    object HideHeader: RanobeReaderEvent
    object ShowHeader: RanobeReaderEvent
}