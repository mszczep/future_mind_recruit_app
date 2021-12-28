package mszczep.futuremindrecruitapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mszczep.futuremindrecruitapp.api.NetworkApi
import mszczep.futuremindrecruitapp.data.RecruitmentData
import mszczep.futuremindrecruitapp.data.RecruitmentDataDao
import mszczep.futuremindrecruitapp.data.RecruitmentDataResponse
import mszczep.futuremindrecruitapp.data.RecruitmentDataState
import mszczep.futuremindrecruitapp.utils.extractUrl
import mszczep.futuremindrecruitapp.utils.formatDate
import java.util.*

class MainActivityViewModel(
    private val networkApi: NetworkApi,
    private val recruitmentDataDao: RecruitmentDataDao
) : ViewModel() {

    private val _recruitmentDataState = MutableLiveData<RecruitmentDataState>()
    val recruitmentDataState: LiveData<RecruitmentDataState> get() = _recruitmentDataState

    private val defaultError = "Something had gone wrong. Please try again later."

    fun getData() {
        _recruitmentDataState.value = RecruitmentDataState.Loading
        viewModelScope.launch {
            val recruitmentData = dbQueryGetRecruitmentData()
            if (recruitmentData.isEmpty())
                synchronizeData()
            else
                _recruitmentDataState.value = RecruitmentDataState.Success(recruitmentData)
        }
    }

    fun refreshData() {
        _recruitmentDataState.value = RecruitmentDataState.Loading
        viewModelScope.launch {
            deleteAllRecruitmentData()
            synchronizeData()
        }
    }

    private suspend fun synchronizeData() {
        val recruitmentData = getNetworkRecruitmentData() ?: return
        val preparedData = ArrayList<RecruitmentData>()
        recruitmentData.forEach {
            val preparedRecruitmentData = prepareDataForInsert(it)
            preparedData.add(preparedRecruitmentData)
        }
        insertRecruitmentData(preparedData)
        _recruitmentDataState.value = RecruitmentDataState.Success(preparedData)
    }

    private suspend fun getNetworkRecruitmentData(): List<RecruitmentDataResponse>? {
        return try {
            networkApi.getRecruitmentData()
        } catch (ex: Exception) {
            _recruitmentDataState.value = RecruitmentDataState.Error(ex.message ?: defaultError)
            null
        }
    }


    private suspend fun deleteAllRecruitmentData() {
        recruitmentDataDao.deleteAllRecruitmentData()
    }

    private fun prepareDataForInsert(recruitmentData: RecruitmentDataResponse): RecruitmentData {
        val url = recruitmentData.description.extractUrl()
        return RecruitmentData(
            0,
            recruitmentData.description,
            recruitmentData.image_url,
            recruitmentData.modificationDate.formatDate(),
            recruitmentData.orderId,
            recruitmentData.title,
            url
        )
    }

    private suspend fun insertRecruitmentData(recruitmentData: List<RecruitmentData>) {
        recruitmentDataDao.insertAll(recruitmentData)
    }

    private suspend fun dbQueryGetRecruitmentData(): List<RecruitmentData> {
        return recruitmentDataDao.getAll()
    }

}