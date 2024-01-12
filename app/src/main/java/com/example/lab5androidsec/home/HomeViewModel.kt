package com.example.lab5androidsec.home

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab5androidsec.data.TagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(val tagsRepository: TagsRepository) : ViewModel(), DefaultLifecycleObserver {
    val uiState = mutableStateOf(UiState())

    fun setImage(uri: Uri) {
        uiState.value = uiState.value.copy(imgSource = uri)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        uiState.value.imgSource?.run{
            setTags()
        }
    }

    fun setTags(){
        viewModelScope.launch {
            uiState.value.imgSource?.let { imgSource ->
                try {
                    val exifData = tagsRepository.getTagsData(imgSource)
                    uiState.value = uiState.value.copy(exifTags = exifData)
                } catch (e: Exception) {
                    // Обработка ошибок, если не удалось получить Exif-данные
                    e.printStackTrace()
                }
            }
        }
    }

    data class UiState(
        val imgSource: Uri? = null,
        val exifTags: Map<String,String> = mapOf()
    )
}