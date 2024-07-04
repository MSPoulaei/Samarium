package ir.tehranshomal.samarium.Services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class LocationService (val context: Context) {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
@SuppressLint("MissingPermission")
fun getLastLocation(): Task<Location> {
        return fusedLocationProviderClient.lastLocation
    }
}