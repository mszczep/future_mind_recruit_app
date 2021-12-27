package mszczep.futuremindrecruitapp.utils

import android.app.Application
import androidx.room.Room
import mszczep.futuremindrecruitapp.data.AppDatabase
import mszczep.futuremindrecruitapp.data.RecruitmentDataDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


/**
 * Koin module responsible for injecting database logic
 */
val dbModule = module {

    /**
     * Database provider; creates a database instance
     * @param application Base application class
     * @return Database instance
     */
    fun provideDB(application: Application): AppDatabase {
       return Room.databaseBuilder(application, AppDatabase::class.java, "future_mind_recruit_app_db")
           .allowMainThreadQueries() //TODO Remove this
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Database interface provider
     * @param db A database instance
     * @return Database interface instance
     */
    fun provideDao(db: AppDatabase): RecruitmentDataDao {
        return db.iTableRecruitmentData()
    }

    single { provideDB(androidApplication()) }
    single { provideDao(get())}

}

