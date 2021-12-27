package mszczep.futuremindrecruitapp.di

import android.app.Application
import androidx.room.Room
import mszczep.futuremindrecruitapp.data.AppDatabase
import mszczep.futuremindrecruitapp.data.RecruitmentDataDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    fun provideDB(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "future_mind_recruit_app_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideRecruitmentDataDao(db: AppDatabase): RecruitmentDataDao {
        return db.recruitmentDataDao()
    }

    single { provideDB(androidApplication()) }
    single { provideRecruitmentDataDao(get()) }
}

