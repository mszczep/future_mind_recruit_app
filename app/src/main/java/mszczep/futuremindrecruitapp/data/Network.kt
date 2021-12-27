package mszczep.futuremindrecruitapp.data

import retrofit2.Response
import retrofit2.http.GET

class Network(private val service: NetworkService) {
    suspend fun getRecruitmentTaskData() = service.getRecruitmentTaskData()

}

interface NetworkService {

    @GET("/recruitment-task")
    suspend fun getRecruitmentTaskData(): Response<List<RecruitmentDataResponse>>
}