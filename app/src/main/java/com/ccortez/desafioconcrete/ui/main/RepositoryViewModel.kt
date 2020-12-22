package com.ccortez.desafioconcrete.ui.main

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.ccortez.desafioconcrete.data.repository.GitHubRepository
import com.ccortez.desafioconcrete.model.PullRequest
import com.ccortez.desafioconcrete.model.Repositories
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryViewModel @Inject constructor(
    gitHubRepository: GitHubRepository,
    application: Application
) : AndroidViewModel(application) {

    lateinit var observableRepository: LiveData<List<PullRequest>>
    lateinit var repositoryID: MutableLiveData<String>

    fun repositoryID(repositoryID: String?) {
        this.repositoryID.value = repositoryID
    }

    companion object {
        private val TAG = RepositoryViewModel::class.java.name
        private val ABSENT: MutableLiveData<*> = MutableLiveData<Any?>()
    }

    init {
        ABSENT.value = null
    }

    init {
        viewModelScope.launch {
            repositoryID = MutableLiveData()
            observableRepository = Transformations.switchMap<String, List<PullRequest>>(
                repositoryID
            ) { input: String ->
                if (input.isEmpty()) {
                    Log.i(
                        TAG,
                        "RepositoryViewModel carID is absent!!!"
                    )
//                return@switchMap ABSENT
                }
                Log.i(
                    TAG,
                    "RepositoryViewModel carID is " + repositoryID.value
                )
                getRepositoryPulls(gitHubRepository)
            }
        }
    }

    fun getRepositoryPulls(gitHubRepository: GitHubRepository) : LiveData<List<PullRequest>?> {
        return gitHubRepository.getPulls(repositoryID.value)
    }
}
