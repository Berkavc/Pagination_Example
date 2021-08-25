package com.scorp.pagination_example.feature

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scorp.domain.DataSource
import com.scorp.domain.FetchCompletionHandler
import com.scorp.domain.Person
import com.scorp.pagination_example.di.qualifiers.DataSourceQualifier
import com.scorp.pagination_example.utility.CustomMutableSingleLiveData
import com.scorp.pagination_example.utility.default
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val appContext: Application,
    @DataSourceQualifier private val dataSource: DataSource
) : ViewModel() {

    var mutableListPerson = CustomMutableSingleLiveData<MutableList<Person?>>().default(
        null
    )

    private var currentMutableList : MutableList<Person?> = mutableListOf()

    val isLoading = CustomMutableSingleLiveData<Boolean>().default(
    false)

    var errorText:String? = null

    val isErrorState = CustomMutableSingleLiveData<Boolean>().default(
        false)

    var isRefreshClicked = CustomMutableSingleLiveData<Boolean>().default(
        false)

    fun refreshClicked(){
        isRefreshClicked.postValue(true)
    }
    internal fun gatherPersonList() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val fetchCompletionHandler: FetchCompletionHandler = { fetchResponse, fetchError ->
                fetchResponse?.let {fetched ->
                    val nonDuplicatedFetchedList:MutableList<Person?> = mutableListOf()
                    fetched.people.forEach { fetch ->
                        var nonDuplicatedPerson:Person? = fetch
                        for(currentListPerson in currentMutableList){
                            if(fetch.id == currentListPerson?.id){
                                nonDuplicatedPerson = null
                                break
                            }
                        }
                        nonDuplicatedPerson?.let {
                            nonDuplicatedFetchedList.add(it)
                        }
                    }
                    currentMutableList.addAll(nonDuplicatedFetchedList)
                    mutableListPerson.postValue(currentMutableList)
                }

                fetchError?.let {
                    errorText = it.errorDescription
                    isErrorState.postValue(true)
                }
                isLoading.postValue(false)

            }
            dataSource.fetch(
                null,
                fetchCompletionHandler
            )
        }

    }
}