package com.dhethi.jntuhconnect.presentation.pdf

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.use_case.load_pdf.LoadPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(
    private val loadPdfUseCase: LoadPdfUseCase
) : ViewModel() {

    private val _state = mutableStateOf(PdfState())
    val state: State<PdfState> = _state

    fun loadPdf( url: String) {
        viewModelScope.launch {
            loadPdfUseCase( url).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            bitmaps = result.data ?: emptyList(),
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}
