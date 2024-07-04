package ir.tehranshomal.samarium.Services

import android.content.Context
import android.location.Location
import androidx.compose.runtime.snapshots.SnapshotStateList
import ir.tehranshomal.samarium.DB.PointDAO
import ir.tehranshomal.samarium.model.Point
import kotlinx.coroutines.delay

suspend fun startFetchingInfo(
    timeInterval: Int,
    context: Context,
    points: SnapshotStateList<Point>,
    pointsDAO: PointDAO,
    unknownPoints: MutableList<Point>,
    locationService: LocationService
) {
    var location:Location?=null
    while (true){
        locationService.getLastLocation().addOnSuccessListener {
            if (it != null){
                location = it
            }
        }
        val newPoint= getServingCellParameters(context)
        if(newPoint==null) {}
        else {
            if (location != null) {
                if (points.isNotEmpty() && points[points.count()-1].timestamp==location!!.time){
                    unknownPoints.add(newPoint)
                }
                else{
                    if(unknownPoints.isNotEmpty()){
                        val point_start=points[points.count()-1]
                        val lat_start=point_start.latitude
                        val long_start=point_start.longitude

                        val lat_end=location!!.latitude
                        val long_end=location!!.longitude

                        val delta_lat=(lat_end-lat_start)/(unknownPoints.count()+1)
                        val delta_long=(long_end-long_start)/(unknownPoints.count()+1)

                        for (i in 0 until unknownPoints.count()) {
                            unknownPoints[i].latitude=lat_start+ delta_lat*(i+1)
                            unknownPoints[i].longitude=long_start + delta_long*(i+1)
                        }
                        points.addAll(unknownPoints)
                        pointsDAO.insertAll(unknownPoints)
                        unknownPoints.clear()
                    }
                    newPoint.latitude = location!!.latitude
                    newPoint.longitude = location!!.longitude
                    newPoint.timestamp=location!!.time
                    points.add(newPoint)
                    pointsDAO.insertAll(newPoint)
                }
            }else{ }
        }
        delay(timeInterval*1000L)
    }
}
