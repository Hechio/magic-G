package com.stevehechio.apps.magictheg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import com.stevehechio.apps.magictheg.data.repository.SetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by stevehechio on 11/27/21
 */
@HiltViewModel
class SetsViewModel @Inject constructor(val setsRepository: SetsRepository): ViewModel() {
    @ExperimentalPagingApi
    fun fetchMagicSets(): Flow<PagingData<SetsEntity>>{
        return setsRepository.getSetResults().cachedIn(viewModelScope)
    }
}