package ir.tehranshomal.samarium.DB

import android.content.Context
import androidx.room.Room

class DbHelper {
    fun getDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "SamariumDB"
        ).build()
    }
    fun getDAO(db: AppDatabase): PointDAO {
        return db.userDao()
    }
}