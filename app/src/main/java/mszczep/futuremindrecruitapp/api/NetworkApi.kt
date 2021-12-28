package mszczep.futuremindrecruitapp.api

import mszczep.futuremindrecruitapp.data.RecruitmentDataResponse
import retrofit2.http.GET

class NetworkApi(private val networkService: NetworkService) {
    suspend fun getRecruitmentData() = networkService.getRecruitmentData()
}

interface NetworkService {

    @GET("/recruitment-task")
    suspend fun getRecruitmentData(): List<RecruitmentDataResponse>
}