package mszczep.futuremindrecruitapp.viewModel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mszczep.futuremindrecruitapp.model.Requests
import mszczep.futuremindrecruitapp.model.db.ITableRecruitmentData
import mszczep.futuremindrecruitapp.model.db.TableRecruitmentData
import java.lang.IllegalStateException
import java.net.UnknownHostException

class MainActivityViewModel(
    private val mRequests: Requests,
    private val iTableRecruitmentData: ITableRecruitmentData
) : ViewModel() {

    /**
     * Progress bar visibility
     */
    private val mProgressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> get() = mProgressBar

    /**
     * Progress bar description
     */
    private val mProgressText = MutableLiveData<String>()
    val progressText: LiveData<String> get() = mProgressText

    /**
     * Error visibility
     */
    private val mErrorHandler = MutableLiveData<Boolean>()
    val errorHandler: LiveData<Boolean> get() = mErrorHandler

    /**
     * A snackbar displaying various messages to the user
     */
    private val mDisplaySnackbar = MutableLiveData<String>()
    val displaySnackbar: LiveData<String> get() = mDisplaySnackbar

    private val mRecruitmentData = MutableLiveData<List<TableRecruitmentData>>()
    val recruitmentData: LiveData<List<TableRecruitmentData>> get() = mRecruitmentData

    private val mGetWebRecruitmentTaskData = MutableLiveData<Boolean>()
    val getWebRecruitmentTaskData: LiveData<Boolean> get() = mGetWebRecruitmentTaskData

    /**
     * Downloading recruitment task data from web
     */
    fun getWebRecruitmentTaskData() {
        viewModelScope.launch {
            Log.d("future_mind_debug","Start getWeb")
            try {
                val response = mRequests.getRecruitmentTaskData()
                if (response.code() != 200 || response.body().isNullOrEmpty()) {
                    mErrorHandler.value = true
//                        "An error has occurred during data download. Please try again"
                } else {
                    Log.d("future_mind_debug","GetWeb got some body here")
                    response.body()!!.forEach {
                        val splitLink = extractLink(it.description)
                        val description = if (splitLink != null) splitLink[0] else it.description
                        val extractedLink = if (splitLink != null) splitLink[1] else null
                        iTableRecruitmentData.insertNewData(
                            TableRecruitmentData(
                                0,
                                description,
                                it.image_url,
                                it.modificationDate,
                                it.orderId,
                                it.title,
                                extractedLink
                            )
                        )
                    }
                    Log.d("future_mind_debug","Should be inserted now")
                    mGetWebRecruitmentTaskData.value = true
                }
            } catch (ex: UnknownHostException) {
                mDisplaySnackbar.value = "Error connecting to the internet. Check your connection."
                ex.printStackTrace()
            } catch (ex: Exception) {
                mDisplaySnackbar.value = "Something had gone wrong. Try again."
                ex.printStackTrace()
            }
        }
    }

    /**
     * Link extraction from text
     * @param string Entry string
     * @return Array of strings
     */
    private fun extractLink(string: String): List<String>? {
        val splitString = string.split("\t")
        Log.d("future_mind_debug","Link extraction: ${splitString[1]}")
        return if (splitString.size == 2) splitString else null
    }

    /**
     * DB query for data
     */
    fun queryDBGetRecruitmentTaskData() {
        launchDataLoad {
            Log.d("future_mind_debug","DB query for data")
            val data = iTableRecruitmentData.getAll()
            Log.d("future_mind_debug","Got some data here: ${data.size}")
            mRecruitmentData.value = data
        }
    }


    /**
     * A helper function displaying a progress bar while loading data
     */
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                mProgressBar.value = true
                block()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            } finally {
                mProgressBar.value = false
            }
        }
    }

}