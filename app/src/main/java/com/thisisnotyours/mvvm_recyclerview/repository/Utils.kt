package com.thisisnotyours.mvvm_recyclerview.repository

import android.util.Log
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import kotlinx.coroutines.delay

// suspend fun 함수는 '코루틴 스코프'내에서만 호출가능.
// retry는 별도의 유틸리티 클래스에 위치시키는 것이 좋다 =>  재사용성을 높이고 코드의 구조를 보다 명확하게 하기위함!

object Utils {
    suspend fun <T> retry(
        maxRetries: Int,
        delayTime: Long,
        block: suspend () -> Resource<T>
    ): Resource<T> {
        repeat(maxRetries) { retryCount ->
            when (val result = block()) {   //block() == mainRepository.getCarListData(token, dataMap, paging)
                is Resource.Error -> {
                    if (retryCount == maxRetries - 1) {
                        Log.d("RetryFunction", "Failed after $maxRetries tries")
                        return result
                    }else{
                        Log.d("RetryFunction", "Retry count: $retryCount")
                        delay(delayTime)
                    }
                }
                else -> {
                    Log.d("RetryFunction", "Success after ${retryCount + 1} tries")
                    return result
                }
            }
        }
        throw IllegalStateException("Unexpected program state")
    }//retry
}


//Question:
//내가 알고싶은 것은 retry 함수가 무조건 실행이 되냐는 것?
//mainRepository.getCarListData(token, dataMap, paging)를 실행해서 데이터를 못가져 왔을 때만 retry  함수를 돌려야 되는거 아님?


//Answers:
//retry 함수는 mainRepository.getCarListData(token, dataMap, paging)를 실행하고 그 결과에 따라 재시도를 결정합니다.
//따라서 retry 함수는 항상 호출되지만, 실제로 재시도가 발생하는 것은 mainRepository.getCarListData(token, dataMap, paging)의 호출이 실패했을 때입니다.

//retry 함수 내부에서 block()이 호출되는 것을 보면, 이 block이 바로 mainRepository.getCarListData(token, dataMap, paging)를 나타냅니다.
// 이 함수 호출이 Resource.Error를 반환하면 재시도를 하고, 그렇지 않으면 (즉, 성공적으로 데이터를 가져오면) 재시도 없이 결과를 반환합니다.

//따라서 답변은 "예"입니다, retry 함수는 항상 실행되지만 실제 재시도가 이루어지는 것은 mainRepository.getCarListData(token, dataMap, paging) 함수 호출이 실패했을 때입니다.