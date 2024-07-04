package ir.tehranshomal.samarium.model

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ir.tehranshomal.samarium.MainActivity

class Location (activityArg: MainActivity) {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activityArg)
    private  var activity : MainActivity = activityArg
    fun getLocation() : Array<Double> {
        // validate permissions
        if(ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity , android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION) , 100)
            return emptyArray()
        }
        val location = fusedLocationProviderClient.lastLocation
        var result = emptyArray<Double>()
        location.addOnSuccessListener {
            if (it != null){
                result = arrayOf(it.longitude , it.latitude)
            }
        }
        return  result
    }
}