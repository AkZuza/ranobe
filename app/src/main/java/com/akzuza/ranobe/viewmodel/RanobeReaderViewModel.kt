package com.akzuza.ranobe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akzuza.ranobe.data.Ranobe
import com.akzuza.ranobe.data.RanobeHomeState
import com.akzuza.ranobe.data.RanobeReaderState
import com.akzuza.ranobe.data.events.RanobeReaderEvent
import com.akzuza.ranobe.data.repositories.LocalRanobeRepository
import com.akzuza.ranobe.data.repositories.RanobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RanobeReaderViewModel @Inject constructor(
    private val context: Context,
    private val ranobeRepository: RanobeRepository
) : ViewModel() {

    val _uiState = MutableStateFlow(RanobeReaderState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: RanobeReaderEvent) {

        when (event) {
            is RanobeReaderEvent.CloseRanobe -> {
                val ranobe = event.ranobe

                viewModelScope.launch {
                    ranobeRepository.updateRanobe(
                        ranobe
                    )
                }

                // Update progress here and close

                _uiState.update {
                    it.copy(
                        ranobe = null,
                    )
                }
            }

            is RanobeReaderEvent.OpenRanobe -> {
                val id = event.id
                viewModelScope.launch {
                    val ranobe = ranobeRepository.getRanobe(id)
                    ranobe?.let {
                        _uiState.update {
                            it.copy(
                                ranobe = ranobe,
                            )
                        }
                    }
                }
            }


            is RanobeReaderEvent.SetCurrentPageNo -> {
                val pageno = event.pageNo
                _uiState.update {
                    it.copy(currentPage = pageno)
                }
            }

            is RanobeReaderEvent.SetTotalPageCount -> {
                val pageCount = event.pageCount
                _uiState.update {
                    it.copy(totalPages = pageCount)
                }
            }

            is RanobeReaderEvent.SetInitialPage -> {
                val initial = event.page
                _uiState.update {
                    it.copy(
                        initialPage = initial
                    )
                }
            }

            RanobeReaderEvent.HideHeader -> {
                _uiState.update { it.copy(showHeader = false) }
            }
            RanobeReaderEvent.ShowHeader -> {
                _uiState.update { it.copy(showHeader = true) }
            }
        }

    }

}