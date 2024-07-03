package ir.tehranshomal.samarium.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable;
import androidx.compose.runtime.remember;
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier;
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import ir.tehranshomal.samarium.model.Point

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;





data class ScorePoint(val lat: Double, val lon: Double, val score: Int)
public class MapView2 {

    @Composable
    fun MapScreen(context: Context, points: SnapshotStateList<Point>) {
        val mapView = remember { MapView(context) }

        AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
        ) { map ->
                map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(5.0)
            map.controller.setCenter(GeoPoint(0.0, 0.0))

            // Sample data
//            val scorePoints = listOf(
//                    ScorePoint(51.5074, -0.1278, 80), // London
//                    ScorePoint(48.8566, 2.3522, 60),  // Paris
//                    ScorePoint(40.7128, -74.0060, 30) // New York
//            )
            val scorePoints=points.map { p -> ScorePoint(p.latitude,p.longitude,p.signalStrength) }

            addMarkersToMap(context, map, scorePoints)
        }
    }

    fun addMarkersToMap(context: Context, map: MapView, scorePoints: List<ScorePoint>) {
        scorePoints.forEach { point ->
                val marker = Marker(map)
            marker.position = GeoPoint(point.lat, point.lon)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            val color = getColorForScore(point.score)
            val icon: Drawable = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.marker_default)!!.mutate()
            DrawableCompat.setTint(icon, color)
            marker.icon = icon

            marker.title = "Score: ${point.score}"

            map.overlays.add(marker)
        }
        map.invalidate()
    }

    fun getColorForScore(score: Int): Int {
        val red = (255 * (100 - score) / 100).coerceIn(0, 255)
        val green = (255 * score / 100).coerceIn(0, 255)
        return android.graphics.Color.rgb(red, green, 0)
    }
}
