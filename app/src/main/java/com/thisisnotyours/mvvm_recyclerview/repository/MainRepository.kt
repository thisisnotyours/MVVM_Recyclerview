package com.thisisnotyours.mvvm_recyclerview.repository

import com.google.gson.JsonObject
import com.thisisnotyours.mvvm_recyclerview.response.CarListResponse
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import com.thisisnotyours.mvvm_recyclerview.retrofit.RetrofitAPI
import com.thisisnotyours.mvvm_recyclerview.retrofit.RetrofitHelper
import kotlinx.coroutines.delay
import retrofit2.Response
import java.lang.Exception

class MainRepository {
    suspend fun getCarListData(token: String, dataMap: Map<String, Any>, paging: JsonObject): Resource<CarListResponse> {
        return try {
            val response: Response<CarListResponse> = RetrofitHelper.instance
                .create(RetrofitAPI::class.java)
                .getCarListData(token, dataMap, paging)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it)
                } ?: Resource.Error(Throwable("Error 응답본문 null: ${response.code()}"))
            }else{
                Resource.Error(Throwable("Server Error 서버오류: ${response.code()}"))
            }

        }catch (e: Exception) {
            Resource.Error(Throwable("Error Exception: $e"))
        }
    }

}