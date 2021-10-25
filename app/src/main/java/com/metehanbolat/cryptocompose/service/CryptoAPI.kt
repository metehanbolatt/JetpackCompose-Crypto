package com.metehanbolat.cryptocompose.service

import com.metehanbolat.cryptocompose.model.Crypto
import com.metehanbolat.cryptocompose.model.CryptoList
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoAPI {

    @GET("prices")
    suspend fun getCryptoList(
        @Query("key") key: String
    ) : CryptoList

    @GET("currencies")
    suspend fun getCrypto(
        @Query("key") key: String,
        @Query("ids") id: String,
        @Query("attributes") attributes: String
    ) : Crypto

}