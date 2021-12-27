package mszczep.futuremindrecruitapp.di

import mszczep.futuremindrecruitapp.data.Network
import mszczep.futuremindrecruitapp.viewmodels.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory { Network(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
}


