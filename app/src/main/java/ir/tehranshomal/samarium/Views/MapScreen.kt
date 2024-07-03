package ir.tehranshomal.samarium.Views

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ir.tehranshomal.samarium.R
import ir.tehranshomal.samarium.model.Point
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    mapView: MapView,
    paddingValues: PaddingValues,
    points: SnapshotStateList<Point>,
    setMarkerListener: (Marker,Point)->Unit,
//    setShowDialog: (Boolean) -> Unit,
//    setSelectedPoint: (Point) -> Unit,
) {
    val context = LocalContext.current
    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) { map ->

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(5.0)
        map.controller.setCenter(GeoPoint(0.0, 0.0))
        map.setMultiTouchControls(true)
        // Add my location overlay
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)

        points.forEach { point ->
            val marker = Marker(mapView)
            marker.position = GeoPoint(point.latitude, point.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

//            val color = getColorForScore(point.signalStrength)
            val icon: Drawable =
                ContextCompat.getDrawable(context, getMarkerDrawable(point.signalStrength))!!
                    .mutate()
//            icon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            marker.icon = icon

            marker.title = point.toString()//"Signal: ${point.signalStrength}"
//            setMarkerListener(marker,point)

            mapView.overlays.add(marker)
        }
        mapView.invalidate()

        myLocationOverlay.runOnFirstFix {
            Handler(Looper.getMainLooper()).postDelayed({
                // Center on my location
                // Your Code
                var last=points.lastOrNull()
                if (last==null) last=Point()
                map.controller.animateTo(GeoPoint(last.latitude,last.longitude))
            }, 1000)
        }
    }
}
//fun getMarkerDrawableResource(score: Int): Int {
//    return when {
//        score >= 70 -> R.drawable.marker_good
//        score >= 40 -> R.drawable.marker_yellow
//        else -> R.drawable.marker_red
//    }
//}
fun getMarkerDrawable(x: Int): Int {
    val color = when (x) {
        in -80..Int.MAX_VALUE -> R.drawable.marker_excellent // Excellent
        in -85..-80 -> R.drawable.marker_very_good // Very Good
        in -90..-85 -> R.drawable.marker_good // Good
        in -95..-90 -> R.drawable.marker_fair // Fair
        in -100..-95 -> R.drawable.marker_poor // Poor
        in -105..-100 -> R.drawable.marker_very_poor // Very Poor
        in -110..-105 -> R.drawable.marker_bad // Bad
        in -115..-110 -> R.drawable.marker_very_bad // Very Bad
        in -120..-115 -> R.drawable.marker_awful // Awful
        else -> R.drawable.marker_no_coverage // No Coverage
    }
    return color
}
