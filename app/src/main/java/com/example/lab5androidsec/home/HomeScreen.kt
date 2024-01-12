package com.example.lab5androidsec.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lab5androidsec.ViewModelProvider
import com.example.lab5androidsec.NavigationDestination
import com.example.lab5androidsec.R
import com.example.lab5androidsec.observeLifecycle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleResourceId: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEditor: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)
    // Лаунчер для получения изображения из галереи
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
            // Получение URI изображения
            val uri = result.data!!.data!!

            // Установка изображения в ViewModel
            viewModel.setImage(uri)

            //Установка тегов
            viewModel.setTags()
        }
    }


    val uiState: HomeViewModel.UiState by viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(HomeDestination.titleResourceId)) },
                modifier = modifier
            )
        },
        floatingActionButton = {
            if (uiState.imgSource != null) {
                FloatingActionButton(
                    onClick = {
                        navigateToEditor(Uri.encode(uiState.imgSource!!.toString()))},
                    contentColor = Color.Green) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit tags"
                    )
                }
            }
            else{
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                            type = "image/*"
                        }
                        pickImageLauncher.launch(intent)},
                    contentColor = Color.Green) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Download image"
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeBody(
            imgSource = uiState.imgSource,
            exifTags = uiState.exifTags.toList(),
            modifier = Modifier.padding(innerPadding)
        )

    }


}

@Composable
fun HomeBody(
    imgSource: Uri?,
    exifTags: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    if (imgSource != null) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imgSource,
                contentDescription = "Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .background(color = Color.White, RectangleShape)
            )
            LazyColumn(
                contentPadding = PaddingValues(8.0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            ) {
                items(items = exifTags) {
                    Row() {
                        Text(
                            text = it.first,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.0.dp))
                        Text(text = it.second)
                    }
                }
            }

        }

    }
}

