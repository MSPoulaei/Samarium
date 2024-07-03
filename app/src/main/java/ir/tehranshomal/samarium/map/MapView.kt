package ir.tehranshomal.samarium.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ir.tehranshomal.samarium.model.Point
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapView {
@Composable
fun MyMapScreen(points: List<Point>) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedPoint by remember { mutableStateOf<Point?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            MapView(context).apply {
                controller.setZoom(5.0)
                controller.setCenter(GeoPoint(51.5074, -0.1278)) // Center to London

                points.forEach { point ->
                    val marker = Marker(this)
                    marker.position = GeoPoint(point.latitude, point.longitude)
                    marker.setOnMarkerClickListener { _, _ ->
                        selectedPoint = point
                        showDialog = true
                        true
                    }
                    overlays.add(marker)
                }
            }
        }, modifier = Modifier.fillMaxSize())

        if (showDialog) {
            selectedPoint?.let { point ->
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Point Information") },
                    text = { Text("ID: ${point.Id}\nCell ID: ${point.cellId}\nSignal Strength: ${point.signalStrength}") },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
}