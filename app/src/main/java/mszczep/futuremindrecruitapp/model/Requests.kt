package mszczep.futuremindrecruitapp.model

import mszczep.futuremindrecruitapp.utils.RecruitmentTaskData
import retrofit2.Response
import retrofit2.http.GET

class Requests(private val service: NetworkService) {
    suspend fun getRecruitmentTaskData() = service.getRecruitmentTaskData()

}

interface NetworkService {

    @GET("/recruitment-task")
    suspend fun getRecruitmentTaskData(): Response<List<RecruitmentTaskData>>
}