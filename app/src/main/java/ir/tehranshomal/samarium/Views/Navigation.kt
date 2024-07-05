package ir.tehranshomal.samarium.Views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    PermissionWrapper { _ ->
        NavHost(navController = navController, startDestination = "map") {
            composable("map") { MainScreen(navController) }
            composable("settings") { SettingsScreen(navController) }
            composable("about-us") { AboutUsPage(navController) }
        }
    }
}