package mszczep.futuremindrecruitapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mszczep.futuremindrecruitapp.data.Network
import mszczep.futuremindrecruitapp.data.RecruitmentDataDao
import mszczep.futuremindrecruitapp.data.RecruitmentData
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val mNetwork: Network,
    private val recruitmentDataDao: RecruitmentDataDao
) : ViewModel() {

    private val mProgressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> get() = mProgressBar

    private val mErrorHandler = MutableLiveData<Pair<Boolean, String?>>()
    val errorHandler: LiveData<Pair<Boolean,String?>> get() = mErrorHandler

    private val mRecruitmentData = MutableLiveData<List<RecruitmentData>>()
    val recruitmentData: LiveData<List<RecruitmentData>> get() = mRecruitmentData

    private val mGetWebRecruitmentTaskData = MutableLiveData<Boolean>()
    val getWebRecruitmentTaskData: LiveData<Boolean> get() = mGetWebRecruitmentTaskData

    fun getWebRecruitmentTaskData() {
        launchDataLoad {
            try {
                val response = mNetwork.getRecruitmentTaskData()
                if (response.code() != 200 || response.body().isNullOrEmpty()) {
                    mErrorHandler.value = Pair(true, "An error has occurred during data download. Please try again")
                } else {
                    response.body()!!.forEach {
                        val splitLink = extractLink(it.description)
                        val description = if (splitLink != null) splitLink[0] else it.description
                        val extractedLink = if (splitLink != null) splitLink[1] else null
                        recruitmentDataDao.insertNewData(
                            RecruitmentData(
                                0,
                                description,
                                it.image_url,
                                formatDate(it.modificationDate) ?: it.modificationDate,
                                it.orderId,
                                it.title,
                                extractedLink
                            )
                        )
                    }
                    mGetWebRecruitmentTaskData.value = true
                }
            } catch (ex: UnknownHostException) {
                mErrorHandler.value = Pair(true, "Error connecting to the internet. Check your connection.")
                ex.printStackTrace()
            } catch (ex: Exception) {
                mErrorHandler.value = Pair(true, "Something had gone wrong. Try again.")
                ex.printStackTrace()
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            deleteAllData()
            getWebRecruitmentTaskData()
        }
    }

    private fun deleteAllData() {
        viewModelScope.launch {
            recruitmentDataDao.deleteAllRecruitmentData()
        }
    }

    private fun extractLink(string: String): List<String>? {
        val splitString = string.split("\t")
        return if (splitString.size == 2) splitString else null
    }

    private fun formatDate(string: String): String? {
        return try {
            val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sdfOutput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = sdfInput.parse(string)
            sdfOutput.format(date)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            null
        }
    }

    fun queryDBGetRecruitmentTaskData() {
        launchDataLoad {
            val data = recruitmentDataDao.getAll()
            mRecruitmentData.value = data
        }
    }


    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                mProgressBar.value = true
                mErrorHandler.value = Pair(false, null)
                block()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            } finally {
                mProgressBar.value = false
            }
        }
    }

}