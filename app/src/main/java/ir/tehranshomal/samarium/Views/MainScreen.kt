package ir.tehranshomal.samarium.Views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ir.tehranshomal.samarium.DB.DbHelper
import ir.tehranshomal.samarium.Services.LocationService
import ir.tehranshomal.samarium.Services.PreferenceManager
import ir.tehranshomal.samarium.Services.startFetchingInfo
import ir.tehranshomal.samarium.model.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var showMenu by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    val preferenceManager = remember { PreferenceManager(context) }
    val locationService=LocationService(context)
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    val timeInterval by remember { mutableStateOf(preferenceManager.getInt("time_interval", 10)) }
    val pointsDAO=DbHelper().getDb(context).pointDAO()
    val points = remember {
        mutableStateListOf<Point>().apply {
            addAll(
                pointsDAO.getAll()
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
                actions = {
                    IconButton(onClick = {
                        if(!started) {
                            scope.launch {
                                try {
                                    startFetchingInfo(
                                        timeInterval, context,
                                        points, pointsDAO,
                                        mutableListOf<Point>(),
                                        locationService
                                    )
                                } catch (e: CancellationException) {
                                    println("Coroutine was cancelled")
                                    started=false
                                }
                            }
                            started=true
                        }else{
                            scope.cancel()
                            started=false
                        }
                    }) {
                        if (started)
                            Icon(Icons.Default.Close, contentDescription = "Stop")
                        else
                            Icon(Icons.Default.PlayArrow, contentDescription = "Start")

                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate("settings")
                            showMenu = false
                        }, text = {
                            Text("Settings")
                        })
                        DropdownMenuItem(onClick = {
                            navController.navigate("about-us")
                            showMenu = false
                        }, text = {
                            Text("About Us")
                        })
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MapScreen(mapView, paddingValues, points)
        }

    }
}