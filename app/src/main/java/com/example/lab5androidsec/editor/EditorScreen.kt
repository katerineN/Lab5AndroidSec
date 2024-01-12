package com.example.lab5androidsec.editor

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab5androidsec.NavigationDestination
import com.example.lab5androidsec.R
import com.example.lab5androidsec.ViewModelProvider
import kotlin.math.absoluteValue

object EditorDestination : NavigationDestination {
    override val route = "editor"
    override val titleResourceId: Int = R.string.edit_tags
    val uriArg = "uri"
    val fullRoute = "$route/{$uriArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = viewModel(factory = ViewModelProvider.Factory)
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(EditorDestination.titleResourceId)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    keyboardController?.hide()
                    viewModel.saveTagsToFile()
                    navigateBack()
                },
                contentColor = Color.Green
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit tags"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        EditorBody(
            viewModel = viewModel
        )
    }
}


@Composable
fun EditorBody(
    viewModel: EditorViewModel,
) {
    val uiState by viewModel.uiState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            CustomTextField(
                value = uiState.latitude,
                label = "Latitude",
                onValueChange = { viewModel.onInputValueChanged(latitude = it) },
                validator = ::validateLatitude,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f, true)
            )
           CustomTextField(
                value = uiState.longitude,
                label = "Longtitude",
                onValueChange = { viewModel.onInputValueChanged(longitude = it) },
                validator = ::validateLongitude,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f, true)
            )
        }

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth()) {
            CustomTextField(
                value = uiState.phoneName,
                label = "Phone name",
                onValueChange = { viewModel.onInputValueChanged(phoneName = it) },
                validator = ::validateName,
                modifier = Modifier.weight(1f, true)
            )
            CustomTextField(
                value = uiState.phoneModel,
                label = "Phone model",
                onValueChange = { viewModel.onInputValueChanged(phoneModel = it) },
                validator = ::validateModel,
                modifier = Modifier.weight(1f, true)
            )
        }

        Spacer(Modifier.height(4.dp))

        CustomTextField(
            value = uiState.date,
            label = "Date",
            onValueChange = { viewModel.onInputValueChanged(date = it) },
            validator = ::validateDate,
            modifier = Modifier.weight(1f, true)
        )
    }

}


@Composable
fun CustomTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    validator: (String) -> Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            )
        },
        singleLine = true,
        modifier = modifier.padding(4.dp),
        isError = !(validator(value) || value.isEmpty()),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    )

}

fun validateLatitude(input: String) = Regex("""-?\d+(\.\d+)*""").matches(input)
        && input.toDouble().absoluteValue <= 90.0

fun validateLongitude(input: String) = Regex("""-?\d+(\.\d+)*""").matches(input)
        && input.toDouble().absoluteValue <= 180.0

fun validateName(input: String) = Regex("""[\wА-Яа-я\- ]+""").matches(input)

fun validateModel(input: String) = Regex("""[\wА-Яа-я\-_+*() ]+""").matches(input)

fun validateDate(input: String) = input.isNotBlank()
