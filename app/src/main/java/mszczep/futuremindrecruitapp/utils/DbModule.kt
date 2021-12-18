package mszczep.futuremindrecruitapp.utils

import android.app.Application
import androidx.room.Room
import mszczep.futuremindrecruitapp.AppDatabase
import mszczep.futuremindrecruitapp.model.db.ITableRecruitmentData
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    fun provideDB(application: Application): AppDatabase {
       return Room.databaseBuilder(application, AppDatabase::class.java, "future_mind_recruit_app_db")
           .allowMainThreadQueries() //TODO Remove this
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(db: AppDatabase): ITableRecruitmentData {
        return db.iTableRecruitmentData()
    }

    single { provideDB(androidApplication()) }
    single { provideDao(get())}

}

