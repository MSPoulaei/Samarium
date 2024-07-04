package ir.tehranshomal.samarium.logic

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationService (val context: Context) {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//    private  var activity : MainActivity = activityArg
@SuppressLint("MissingPermission")
fun getLocation() :Location?{

        val location = fusedLocationProviderClient.lastLocation
        var result:Location?=null
        location.addOnSuccessListener {
            if (it != null){
                result = it
            }
        }
        return  result
    }
}