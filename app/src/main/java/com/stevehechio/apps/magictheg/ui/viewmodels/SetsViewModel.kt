package com.stevehechio.apps.magictheg.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.stevehechio.apps.magictheg.data.repository.SetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by stevehechio on 11/27/21
 */
@HiltViewModel
class SetsViewModel @Inject constructor(val setsRepository: SetsRepository): ViewModel() {

}