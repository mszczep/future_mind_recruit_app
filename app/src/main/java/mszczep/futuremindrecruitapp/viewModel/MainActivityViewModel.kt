package mszczep.futuremindrecruitapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mszczep.futuremindrecruitapp.model.Requests
import mszczep.futuremindrecruitapp.model.db.ITableRecruitmentData

class MainActivityViewModel(val mRequests: Requests, val iTableRecruitmentData: ITableRecruitmentData): ViewModel() {

    fun getRecruitmentTaskData(){
        viewModelScope.launch {
            val response = mRequests.getRecruitmentTaskData()
            val a = iTableRecruitmentData.getAll()
            Log.d("future_mind_debug","Response: $response ++ db response: $a")
            val body = response.body()
//            Log.d("future_mind_debug","Body: $body")
        }
    }

}