package ir.tehranshomal.samarium.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.tehranshomal.samarium.model.Point

@Database(entities = [Point::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PointDAO
}