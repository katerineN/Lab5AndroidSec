package com.example.lab5androidsec

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab5androidsec.data.AppContainer
import com.example.lab5androidsec.data.AppDataContainer
import com.example.lab5androidsec.editor.EditorDestination
import com.example.lab5androidsec.editor.EditorScreen
import com.example.lab5androidsec.home.HomeDestination
import com.example.lab5androidsec.home.HomeScreen

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun Lab5App(navController: NavHostController = rememberNavController()) {
    Lab5NavHost(navController = navController)
}

class Lab5Application : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

interface NavigationDestination {
    val route: String
    val titleResourceId: Int
}

@Composable
fun Lab5NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToEditor = {
                    navController.navigate("${EditorDestination.route}/${it}")
                },
            )
        }
        composable(route = EditorDestination.fullRoute,
            arguments = listOf(
                navArgument(EditorDestination.uriArg){
                    type = NavType.StringType
                }
            )
        ) {
            EditorScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}