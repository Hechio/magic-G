package com.stevehechio.apps.magictheg.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stevehechio.apps.magictheg.data.Resource
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.repository.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by stevehechio on 11/28/21
 */
@HiltViewModel
class CardsViewModel @Inject constructor(val repository: CardsRepository): BaseViewModel() {
    @ExperimentalPagingApi
   fun fetchMagicCards(setCode: String): Flow<PagingData<CardsEntity>> {
        return repository.getCardsResults(setCode).map {
            it.map { cardsForSet -> cardsForSet }
        }.cachedIn(viewModelScope)
    }

    private val boostersLiveData: MutableLiveData<Resource<List<CardsBoosterEntity>>> =
        MutableLiveData<Resource<List<CardsBoosterEntity>>>()

    fun getBoosterLiveData(): MutableLiveData<Resource<List<CardsBoosterEntity>>> {
        return boostersLiveData
    }

    fun fetchBoosterCards(setCode: String){
        addToDisposable(repository.fetchBoosterCards(setCode).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                boostersLiveData.postValue(it)
                Log.v("Home VM Success", "Success Execution! $it")
            },{
                boostersLiveData.postValue(Resource.Failure(it.localizedMessage))
                Log.v("Home VM error", "Success Execution! $it")
            }) )
    }
}