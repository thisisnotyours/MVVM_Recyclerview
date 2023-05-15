package com.thisisnotyours.mvvm_recyclerview.repository

import com.thisisnotyours.mvvm_recyclerview.response.LoginResponse
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import com.thisisnotyours.mvvm_recyclerview.retrofit.RetrofitAPI
import com.thisisnotyours.mvvm_recyclerview.retrofit.RetrofitHelper
import okhttp3.RequestBody
import retrofit2.Response
import java.lang.Exception

class LoginRepository {

    suspend fun getLoginData(requestBody: RequestBody): Resource<LoginResponse> {
        return try {
            val response: Response<LoginResponse> = RetrofitHelper.instance
                .create(RetrofitAPI::class.java)
                .getLoginData(requestBody)

            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it, response.headers())  //edit: 헤더를 꼭 넘겨줘야 토큰 받음 **
                } ?: Resource.Error(Throwable("Error: ${response.code()} 응답본문 null"))
            }else{
                Resource.Error(throw Exception("서버오류 Server Error: ${response.code()}"))
            }

        }catch (e: Exception) {
            Resource.Error(Throwable("Error Exception: $e"))
        }
    }
}


//일시중지(suspend)를 하는이유: 비동기 작업을 수행하기 위해.
//suspend 키워드를 사용하면 코루틴이 일시 중단되고 다른 코루틴으로 제어 이전 가능.

//edit:
// getLoginData 메소드에서 suspend 키워드를 사용하면 Retrofit 이 백그라운드에서 HTTP 요청을 보내고
// 서버로부터 응답을 받을 때까지 일시 중지되며, 이를통해 UI 스레드의 블로킹을 방지할 수 있음.
// 코루틴을 사용하면 일시 중단된 상태에서 다른 작업을 수행할 수 있으므로 UI가 멈추지 않음.
// 일시 중단된 작업이 다시 시작되면 중단된 지점부터 다시 시작.
//edit: 그리고 서버로부터 받은 데이터를 Resource 클래스로 래핑아여 반환(return Resource.Success(response) 또는 Resource.Error(Exception)).



//edit: Response<LoginResponse>와 LoginResponse의 차이
//
//Response<LoginResponse>:
//
//Retrofit 라이브러리에서 제공하는 Response 클래스를 사용합니다.
//HTTP 응답의 메타데이터(상태 코드, 헤더 등)와 함께 본문 데이터를 포함합니다.
//성공적인 응답과 오류 응답을 모두 처리할 수 있습니다. 이를 통해 HTTP 상태 코드, 헤더 등과 같은 추가 정보를 확인할 수 있습니다.
//LoginResponse:
//
//단순히 API 응답의 본문 데이터만 포함합니다.
//HTTP 응답의 메타데이터(상태 코드, 헤더 등)에 대한 정보는 사용할 수 없습니다.
//따라서, Response<LoginResponse>를 사용하면 API 응답의 본문 뿐만 아니라 메타데이터도 접근할 수 있어 더 유연한 처리가 가능합니다.
// 반면, LoginResponse를 사용하면 API 응답의 본문 데이터만 처리할 수 있습니다.