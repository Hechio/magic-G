package com.stevehechio.apps.magictheg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.repository.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by stevehechio on 11/28/21
 */
@HiltViewModel
class CardsViewModel @Inject constructor(val repository: CardsRepository): ViewModel() {
    @ExperimentalPagingApi
   fun fetchMagicCards(setCode: String): Flow<PagingData<CardsEntity>> {
        return repository.getCardsResults(setCode).map {
            it.map { cardsForSet -> cardsForSet }
        }.cachedIn(viewModelScope)
    }
}