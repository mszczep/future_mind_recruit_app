package mszczep.futuremindrecruitapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mszczep.futuremindrecruitapp.model.Requests

//class MainActivityViewModel(application: Application): AndroidViewModel(application) {
class MainActivityViewModel(val mRequests: Requests): ViewModel() {

//    private val mRequests = Requests

    fun getRecruitmentTaskData(){
        viewModelScope.launch {
            val response = mRequests.getRecruitmentTaskData()
            Log.d("future_mind_debug","Response: $response")
            val body = response.body()
            Log.d("future_mind_debug","Body: $body")

        }
    }

}