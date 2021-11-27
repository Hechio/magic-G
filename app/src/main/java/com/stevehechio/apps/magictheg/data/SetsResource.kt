package com.stevehechio.apps.magictheg.data

import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity

/**
 * Created by stevehechio on 11/27/21
 */
sealed class SetsResource {
    data class Success(val data: List<SetsEntity>): SetsResource()
    data class Error(val error: Exception): SetsResource()
}
