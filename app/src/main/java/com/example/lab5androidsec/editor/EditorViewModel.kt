package com.example.lab5androidsec.editor

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab5androidsec.data.TagsRepository
import kotlinx.coroutines.launch

class EditorViewModel(savedStateHandle: SavedStateHandle,
                      private val tagsRepository: TagsRepository
):ViewModel(), DefaultLifecycleObserver {
    private val iUri: Uri = Uri.parse(savedStateHandle[EditorDestination.uriArg]!!)

    val uiState = mutableStateOf(UiState())

    init {
        viewModelScope.launch {
            uiState.value = tagsRepository.getEditableTags(iUri).toUiState()
        }
    }

    fun onInputValueChanged(date: String? = null,
                            latitude: String? = null,
                            longitude: String? = null,
                            phoneName: String? = null,
                            phoneModel: String? = null){
        date?.let {
            uiState.value = uiState.value.copy(date = date)
        }
        latitude?.let {
            uiState.value = uiState.value.copy(latitude = latitude)
        }
        longitude?.let {
            uiState.value = uiState.value.copy(longitude = longitude)
        }
        phoneName?.let {
            uiState.value = uiState.value.copy(phoneName = phoneName)
        }
        phoneModel?.let {
            uiState.value = uiState.value.copy(phoneModel = phoneModel)
        }
    }

    fun saveTagsToFile(){
        viewModelScope.launch {
            tagsRepository.saveTagsData(iUri, uiState.value.toMap())
        }
    }


    data class UiState(
        val date: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val phoneName: String = "",
        val phoneModel: String = ""
    )
}

fun Map<String, String>.toUiState() : EditorViewModel.UiState{
    return EditorViewModel.UiState(
        this[TagsRepository.TAG_DATETIME] ?: "",
        this[TagsRepository.TAG_LATITUDE] ?: "",
        this[TagsRepository.TAG_LONGITUDE] ?: "",
        this[TagsRepository.TAG_MAKE] ?: "",
        this[TagsRepository.TAG_MODEL] ?: ""
    )
}

fun EditorViewModel.UiState.toMap() : Map<String, String>{
    return mapOf(
        TagsRepository.TAG_DATETIME to date,
        TagsRepository.TAG_MAKE to phoneName,
        TagsRepository.TAG_MODEL to phoneModel,
        TagsRepository.TAG_LATITUDE to latitude,
        TagsRepository.TAG_LONGITUDE to longitude
    )

}