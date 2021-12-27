package mszczep.futuremindrecruitapp

import android.app.Application
import mszczep.futuremindrecruitapp.utils.viewModelsModule
import mszczep.futuremindrecruitapp.utils.dbModule
import mszczep.futuremindrecruitapp.utils.retrofitModule
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