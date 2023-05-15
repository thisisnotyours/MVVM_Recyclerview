package com.thisisnotyours.mvvm_recyclerview.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val BASE_URL = "https://intertaxi.co.kr/"

    val instance: Retrofit by lazy {
        OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .build()
            .let { client ->
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
            }
    }
}


//edit: RetrofitHelper 클래스에서 gson 객체를 사용하지 않는 이유는
//GsonConverterFactory를 통해 Retrofit에 이미 등록되어 있기 때문입니다.
//GsonConverterFactory는 Gson을 사용하여 JSON 데이터를 객체로 변환하고,
//객체를 JSON 데이터로 변환하는 역할을 합니다.
//따라서 Retrofit에서 Gson을 직접 사용하지 않아도 됩니다.


//edit: companion object
//는 클래스의 인스턴스가 아닌 클래스 자체에 대한 메소드와 변수를 선언할 때 사용되는 특별한 객체입니다.
//일반적인 객체의 속성과 메서드는 해당 객체를 생성한 인스턴스에 속합니다.
//그러나 companion object는 클래스에 속하는 정적인 속성과 메서드를 선언할 수 있습니다.
//즉, 해당 클래스의 모든 인스턴스가 공유할 수 있는 속성과 메서드를 선언할 수 있습니다.
//예를 들어, 클래스 내에 자주 사용되는 유틸리티 메서드를 companion object로 정의하면
// 인스턴스를 생성하지 않고도 해당 메서드에 접근할 수 있습니다.
// 또한, 클래스 내부에서 companion object를 이용하여 private한 속성과 메소드를 선언할 수도 있습니다.