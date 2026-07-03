package com.dhethi.jntuhconnect.presentation.gracemarks

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.model.GraceEligibility
import com.dhethi.jntuhconnect.domain.model.GraceProofResult
import com.dhethi.jntuhconnect.domain.use_case.grace_marks.CheckGraceEligibilityUseCase
import com.dhethi.jntuhconnect.domain.use_case.grace_marks.UploadGraceProofUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

private const val MAX_BYTES = 5 * 1024 * 1024
private val ALLOWED_TYPES = setOf("application/pdf", "image/png", "image/jpeg", "image/jpg")

data class GraceMarksState(
    val roll: String = "",
    val checkLoading: Boolean = false,
    val checkError: String = "",
    val eligibility: GraceEligibility? = null,
    val uploadLoading: Boolean = false,
    val uploadError: String = "",
    val uploadResult: GraceProofResult? = null
)

@HiltViewModel
class GraceMarksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val checkGraceEligibilityUseCase: CheckGraceEligibilityUseCase,
    private val uploadGraceProofUseCase: UploadGraceProofUseCase
) : ViewModel() {

    private val _state = mutableStateOf(GraceMarksState())
    val state: State<GraceMarksState> = _state

    fun updateRoll(value: String) {
        _state.value = _state.value.copy(roll = value.uppercase().take(10))
    }

    fun checkEligibility() {
        val roll = _state.value.roll
        if (roll.length != 10) {
            _state.value = _state.value.copy(checkError = "Enter a valid 10-character roll number.")
            return
        }
        _state.value = _state.value.copy(uploadResult = null, uploadError = "")
        checkGraceEligibilityUseCase(roll).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(checkLoading = true, checkError = "", eligibility = null)
                is Resource.Error -> _state.value.copy(checkLoading = false, checkError = result.message ?: "Failed")
                is Resource.Success -> _state.value.copy(checkLoading = false, checkError = "", eligibility = result.data)
            }
        }.launchIn(viewModelScope)
    }

    fun uploadProof(uri: Uri) {
        val roll = _state.value.roll
        viewModelScope.launch {
            _state.value = _state.value.copy(uploadLoading = true, uploadError = "", uploadResult = null)
            val prepared = withContext(Dispatchers.IO) { preparePart(uri) }
            when (prepared) {
                is PrepareResult.Error ->
                    _state.value = _state.value.copy(uploadLoading = false, uploadError = prepared.message)

                is PrepareResult.Ok -> {
                    uploadGraceProofUseCase(roll, prepared.part).onEach { result ->
                        _state.value = when (result) {
                            is Resource.Loading -> _state.value.copy(uploadLoading = true)
                            is Resource.Error -> _state.value.copy(uploadLoading = false, uploadError = result.message ?: "Upload failed")
                            is Resource.Success -> _state.value.copy(uploadLoading = false, uploadResult = result.data, uploadError = "")
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    private sealed interface PrepareResult {
        data class Ok(val part: MultipartBody.Part) : PrepareResult
        data class Error(val message: String) : PrepareResult
    }

    private fun preparePart(uri: Uri): PrepareResult {
        return try {
            val resolver = context.contentResolver
            val type = (resolver.getType(uri) ?: "application/octet-stream").lowercase()
            if (type !in ALLOWED_TYPES) {
                return PrepareResult.Error("Only PDF or image (PNG/JPEG) files are accepted.")
            }
            val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return PrepareResult.Error("Couldn't read the selected file.")
            if (bytes.isEmpty()) return PrepareResult.Error("The selected file is empty.")
            if (bytes.size > MAX_BYTES) return PrepareResult.Error("File exceeds the 5MB limit.")

            val name = queryFileName(uri) ?: "proof"
            val body = bytes.toRequestBody(type.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", name, body)
            PrepareResult.Ok(part)
        } catch (e: Exception) {
            PrepareResult.Error("Couldn't read the selected file.")
        }
    }

    private fun queryFileName(uri: Uri): String? {
        return runCatching {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
            }
        }.getOrNull()
    }
}
