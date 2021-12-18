package mszczep.futuremindrecruitapp.utils

import mszczep.futuremindrecruitapp.model.Requests
import mszczep.futuremindrecruitapp.viewModel.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Koin module responsible for injecting dependencies into viewModels
 */
val appModule = module {
    factory { Requests(get()) }
    viewModel { MainActivityViewModel(get(), get()) }
}


