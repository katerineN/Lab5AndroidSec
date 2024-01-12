package com.example.lab5androidsec

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab5androidsec.editor.EditorViewModel
import com.example.lab5androidsec.home.HomeViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                lab5Application().container.tagsRepository
            )
        }
        initializer {
            EditorViewModel(
                this.createSavedStateHandle(),
                lab5Application().container.tagsRepository
            )
        }

    }
}

fun CreationExtras.lab5Application(): Lab5Application =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Lab5Application)

@Composable
fun <LO : LifecycleObserver> LO.observeLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@observeLifecycle)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycle)
        }
    }
}