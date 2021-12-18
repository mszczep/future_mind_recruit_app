package mszczep.futuremindrecruitapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//@Database(
//    entities = [
//        TODO()::class
//    ],
//    version = 1,
//    exportSchema = false
//)

abstract class AppDatabase: RoomDatabase() {
//    abstract fun TODOInterface(): TODOInterface TODO

    companion object {
        @Volatile
        var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        fun getInstance(context: Context): AppDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "future_mind_recruit_app_db")
                .build()
    }
}