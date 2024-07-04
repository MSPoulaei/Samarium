package ir.tehranshomal.samarium.Views

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
import ir.tehranshomal.samarium.Services.getMarkerDrawable
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
) {
    val context = LocalContext.current
    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) { map ->

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(15.0)
        // Add my location overlay
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)

        var last=points.lastOrNull()
        if (last==null) {
            last=Point()
            val mylocation=myLocationOverlay.myLocation
            if (mylocation!=null){
                last.latitude=myLocationOverlay.myLocation.latitude
                last.longitude=myLocationOverlay.myLocation.longitude
            }
            if(last.latitude.toInt() ==0 && last.longitude.toInt() ==0){
                //Iran
                last.latitude=32.4279
                last.longitude=53.6880
            }
        }
        map.controller.setCenter(GeoPoint(last.latitude, last.longitude))

        map.setMultiTouchControls(true)

        points.forEach { point ->
            val marker = Marker(mapView)
            marker.position = GeoPoint(point.latitude, point.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            val icon: Drawable =
                ContextCompat.getDrawable(context, getMarkerDrawable(point.signalStrength))!!
                    .mutate()
            marker.icon = icon

            marker.title = point.toString()

            mapView.overlays.add(marker)
        }
        mapView.invalidate()

        myLocationOverlay.runOnFirstFix {
            Handler(Looper.getMainLooper()).postDelayed({
                // Center on my location
                map.controller.animateTo(GeoPoint(last.latitude,last.longitude))
            }, 1000)
        }
    }
}

