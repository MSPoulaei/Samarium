package ir.tehranshomal.samarium.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.tehranshomal.samarium.model.Point


@Dao
interface PointDAO {
    @Query("SELECT * FROM point")
    fun getAll(): List<Point>

    @Insert
    fun insertAll(vararg points: Point)

    @Insert
    fun insertAll(points: List<Point>)

    @Delete
    fun delete(point: Point)

    @Query("DELETE FROM point")
    fun deleteAll()

}
