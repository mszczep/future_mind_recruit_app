package mszczep.futuremindrecruitapp.di

import mszczep.futuremindrecruitapp.api.NetworkApi
import mszczep.futuremindrecruitapp.viewmodels.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory { NetworkApi(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
}


