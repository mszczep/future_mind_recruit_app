package mszczep.futuremindrecruitapp.model

import mszczep.futuremindrecruitapp.utils.RecruitmentTaskData
import retrofit2.Response
import retrofit2.http.GET

class Requests(val service: IRequests) {
    suspend fun getRecruitmentTaskData() = service.getRecruitmentTaskData()

}

interface IRequests {

    @GET("/recruitment-task")
    suspend fun getRecruitmentTaskData(): Response<List<RecruitmentTaskData>>
}