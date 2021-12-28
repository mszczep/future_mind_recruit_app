package mszczep.futuremindrecruitapp.data

sealed class RecruitmentDataState {
    data class Success(val data: List<RecruitmentData>): RecruitmentDataState()
    data class Error(val errorMessage: String): RecruitmentDataState()
    object Loading: RecruitmentDataState()
}