package com.metehanbolat.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import com.metehanbolat.cryptocompose.model.Crypto
import com.metehanbolat.cryptocompose.repository.CryptoRepository
import com.metehanbolat.cryptocompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoDetailViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel(){

    suspend fun getCrypto(id : String) : Resource<Crypto>{
        return repository.getCrypto(id)
    }
}