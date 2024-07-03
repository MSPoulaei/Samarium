package ir.tehranshomal.samarium.model

import android.telephony.CellSignalStrength
import androidx.core.location.LocationRequestCompat.Quality
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Point(
    @PrimaryKey(autoGenerate = true)
    var Id : Int =0,

    @ColumnInfo(name = "cell_id")
    var cellId : Int =-1,

    @ColumnInfo(name = "mcc")
    var MCC:String ="",
    @ColumnInfo(name = "mnc")
    var MNC:String ="",
    @ColumnInfo(name = "plmnid")
    var PLMNID:String ="",

    @ColumnInfo(name = "technology_name")
    var technologyName:String="",

    @ColumnInfo(name = "technology_number")
    var technologyNumber:Int=-1,

    @ColumnInfo(name = "lac")
    var LAC:Int=-1,

    @ColumnInfo(name = "signal_strength")
    var signalStrength :Int=0,
    @ColumnInfo(name = "signal_quality")
    var signalQuality :Int=0,

    @ColumnInfo(name = "longitude")
    var longitude :Double= .0,
    @ColumnInfo(name = "latitude")
    var latitude :Double=.0,
    )
