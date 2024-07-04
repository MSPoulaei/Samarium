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

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<Point>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): Point

    @Insert
    fun insertAll(vararg points: Point)

    @Insert
    fun insertAll(points: List<Point>)

    @Delete
    fun delete(point: Point)

    @Query("DELETE FROM point")
    fun deleteAll()

}
