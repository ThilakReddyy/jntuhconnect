package com.dhethi.jntuhconnect.presentation.content

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.use_case.get_content.GetContentUseCase
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.domain.model.ContentDoc
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SyllabusViewModel @Inject constructor(
    private val getContentUseCase: GetContentUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = mutableStateOf(ContentTreeState())
    val state: State<ContentTreeState> = _state

    init {
        load()
    }

    fun load() {
        getContentUseCase.syllabus().onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true, error = "")
                is Resource.Success -> ContentTreeState(isLoading = false, root = result.data)
                is Resource.Error ->
                    _state.value.copy(isLoading = false, error = result.message ?: "Something went wrong")
            }
        }.launchIn(viewModelScope)
    }

    fun recordOpened(doc: ContentDoc) {
        viewModelScope.launch {
            appPreferences.recordRecentDocument(
                RecentDocument(doc.title, doc.link, RecentDocument.SYLLABUS, System.currentTimeMillis())
            )
        }
    }
}
