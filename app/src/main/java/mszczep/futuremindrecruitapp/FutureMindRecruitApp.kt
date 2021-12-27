package mszczep.futuremindrecruitapp

import android.app.Application
import mszczep.futuremindrecruitapp.di.viewModelsModule
import mszczep.futuremindrecruitapp.di.dbModule
import mszczep.futuremindrecruitapp.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class FutureMindRecruitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FutureMindRecruitApp)
            modules(viewModelsModule, retrofitModule, dbModule)
        }
    }
}