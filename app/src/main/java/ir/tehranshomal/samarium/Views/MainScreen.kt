package ir.tehranshomal.samarium.Views

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import ir.tehranshomal.samarium.logic.getServingCellParameters
import ir.tehranshomal.samarium.model.Point
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPoint by remember { mutableStateOf<Point?>(null) }
    val pointsDAO=DbHelper().getDb(context).pointDAO()
    val points = remember {
        mutableStateListOf<Point>().apply {
            addAll(
                pointsDAO.getAll()
//                listOf(
//                    Point(1, 101, "432", "10", "43210", "4G", 4, 12345, -70, 2, 51.5074, -0.1278),
//                    Point(2, 102, "432", "11", "43211", "4G", 4, 12346, -65, 3, 48.8566, 2.3522),
//                    Point(3, 103, "432", "12", "43212", "4G", 4, 12347, -80, 1, 40.7128, -74.0060)
//                )
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
                actions = {
                    IconButton(onClick = { /* TODO: Add button action */ }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "More")
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
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newPoint = generateRandomScorePoint(context)
                    if (newPoint==null) return@FloatingActionButton
                    points.add(newPoint)
                    pointsDAO.insertAll(newPoint)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add random point")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            MapScreen(mapView,paddingValues,points) { x, y ->
                run {
                    x.setOnMarkerClickListener { _, _ ->
                        selectedPoint = y
                        showDialog = true
                        true
                    }
                }
            }//{showDialog=it},{selectedPoint=it})
            InfoBox(showDialog,selectedPoint,{showDialog=it})
        }

    }
}


data class ScorePoint(val lat: Double, val lon: Double, val score: Int)


fun getColorForScore(score: Int): Int {
    val red = (255 * (100 - score) / 100).coerceIn(0, 255)
    val green = (255 * score / 100).coerceIn(0, 255)
    return android.graphics.Color.rgb(red, green, 0)
}

fun generateRandomScorePoint(context: Context): Point? {
    val lat = Random.nextDouble(-90.0, 90.0)
    val lon = Random.nextDouble(-180.0, 180.0)
    val score = Random.nextInt(0, 101)
    val point=getServingCellParameters(context)
    point?.apply {
        signalStrength=score
        latitude=lat
        longitude=lon
    }
    return point
}

@Composable
fun InfoBox(showDialog:Boolean,selectedPoint: Point?,onShowDialogChange:(Boolean) -> Unit) {
    if (showDialog) {
        selectedPoint?.let { point ->
            AlertDialog(
                onDismissRequest = { onShowDialogChange(false) },
                title = { Text("Point Information") },
                text = { Text("ID: ${point.Id}\nCell ID: ${point.cellId}\nSignal Strength: ${point.signalStrength}") },
                confirmButton = {
                    Button(onClick = { onShowDialogChange(false) }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}