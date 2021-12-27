package mszczep.futuremindrecruitapp.utils

import mszczep.futuremindrecruitapp.model.Requests
import mszczep.futuremindrecruitapp.viewmodels.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory { Requests(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
}


