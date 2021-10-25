package com.metehanbolat.cryptocompose.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metehanbolat.cryptocompose.model.CryptoListItem
import com.metehanbolat.cryptocompose.repository.CryptoRepository
import com.metehanbolat.cryptocompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    var cryptoList = mutableStateOf<List<CryptoListItem>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var initialCryptoList = listOf<CryptoListItem>()
    private var isSearchingStarting = true

    init {
        loadCryptos()
    }

    fun searchCryptoList(query : String){
        val listToSearch = if (isSearchingStarting){
            cryptoList.value
        }else{
            initialCryptoList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()){
                cryptoList.value = initialCryptoList
                isSearchingStarting = true
                return@launch
            }

            val results = listToSearch.filter {
                it.currency.contains(query.trim(), ignoreCase = true)
            }

            if (isSearchingStarting){
                initialCryptoList = cryptoList.value
                isSearchingStarting = false
            }
            cryptoList.value = results
        }
    }

    fun loadCryptos(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCryptoList()
            when(result){
                is Resource.Success -> {
                    val cryptoItems = result.data!!.mapIndexed { index, cryptoListItem ->
                        CryptoListItem(cryptoListItem.currency, cryptoListItem.price)
                    }
                    errorMessage.value = ""
                    isLoading.value = false
                    cryptoList.value += cryptoItems
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    isLoading.value = false
                }

            }
        }
    }
}