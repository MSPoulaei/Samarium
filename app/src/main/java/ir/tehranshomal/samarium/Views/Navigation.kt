package ir.tehranshomal.samarium.Views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ir.tehranshomal.samarium.logic.PermissionWrapper

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    PermissionWrapper { permissionsState ->
        // Your app's main content goes here
        NavHost(navController = navController, startDestination = "map") {
            composable("map") { MainScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
        }
    }
}