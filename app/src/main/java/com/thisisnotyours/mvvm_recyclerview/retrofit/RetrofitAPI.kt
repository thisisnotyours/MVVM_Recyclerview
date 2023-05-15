package com.thisisnotyours.mvvm_recyclerview.retrofit

import com.google.gson.JsonObject
import com.thisisnotyours.mvvm_recyclerview.response.CarListResponse
import com.thisisnotyours.mvvm_recyclerview.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitAPI {
    @POST("/api/meter/auth/login")
    suspend fun getLoginData(@Body requestBody: RequestBody): Response<LoginResponse>

    @GET("/api/meter/connection")
    suspend fun getCarListData(
        @Header("Cookie") token: String,
        @QueryMap(encoded = true) dataMap: Map<String, @JvmSuppressWildcards Any>,
        @Query("paging") paging: JsonObject
    ): Response<CarListResponse>
}