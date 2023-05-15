package com.thisisnotyours.mvvm_recyclerview.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.thisisnotyours.mvvm_recyclerview.adapter.CarAdapter
import com.thisisnotyours.mvvm_recyclerview.data.Car
import com.thisisnotyours.mvvm_recyclerview.repository.MainRepository
import com.thisisnotyours.mvvm_recyclerview.repository.Utils
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val mainRepository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {  //mainRepository 의존성 주입
    private val TAG = "MainViewModel"

    private val carTypeLiveData = MutableLiveData<Int>(0)
    private val carNumLiveData = MutableLiveData<String>("")
    private val mdnLiveData = MutableLiveData<String>("")

    private val _toastMsgLiveData = MutableLiveData<String>()
    val toastMsgLiveData: LiveData<String>
        get() = _toastMsgLiveData

    private val _carListLiveData = MutableLiveData<Resource<List<Car>>>()
    val carListLiveData: LiveData<Resource<List<Car>>>
        get() = _carListLiveData

    val carAdapter = CarAdapter()

    fun updateCarListRecyclerView(carList: List<Car>) {
        carAdapter.updateCarList(carList)
    }

    fun setCarType(car_type: Int) {
        carTypeLiveData.value = car_type
    }

    fun getCarType(): LiveData<Int> {
        return carTypeLiveData
    }

    fun setCarNum(car_num: String) {
        carNumLiveData.value = car_num
    }

    fun getCarNum(): LiveData<String> {
        return carNumLiveData
    }

    fun setMdn(mdn: String) {
        mdnLiveData.value = mdn
    }

    fun getMdn(): LiveData<String> {
        return mdnLiveData
    }

    fun fetchCarListData(token: String, paging: JsonObject) {
        val carType = carTypeLiveData.value
        val carNum = carNumLiveData.value
        val mdn = mdnLiveData.value

        Log.d(TAG+"_TOKEN", token)
        Log.d(TAG+"_VALUE", "${carType}/ ${carNum}/ ${mdn}")

        val dataMap = mutableMapOf<String, Any>()

        carType?.let { dataMap["car_type"] = it }
        carNum?.let { dataMap["car_num"] = it }
        mdn?.let { dataMap["mdn"] = it }

        viewModelScope.launch {

            _carListLiveData.postValue(Resource.Loading())

//            val response = mainRepository.getCarListData(token, dataMap, paging)
            val response = withContext(Dispatchers.IO) {    //Dispatchers.IO: 입출력 작업에 최적화된 백그라운드 스레드에서 작업을 수행
                Utils.retry(maxRetries = 2, delayTime = 1500L) {
                    mainRepository.getCarListData(token, dataMap, paging)
                }
            }

            when (response) {
                is Resource.Success -> {
                    Log.d(TAG+"_SSUCCESS", "${response.data}")
                    response.data?.let { carListResponse ->
                        val carDataList = carListResponse.data
                        if (carDataList.size == 0) {
                            _toastMsgLiveData.postValue("데이터 없음")
                        } else {
                            val carList = carDataList.map { carData ->
                                Car(
                                    carData.car_num,
                                    carData.mdn,
                                    carData.car_regnum,
                                    carData.company_name
                                )
                            }
                            //liveData 업데이트
                            _carListLiveData.postValue(Resource.Success(carList))
                        }
                    }

                }

                is Resource.Error -> {
                    Log.e(TAG+"_ERROR", "${response.exception?.message}")
                    val errorBoddy = (response.exception as? HttpException)?.response()?.errorBody()?.string()
                    Log.e(TAG+"_ERROR_BODY", "${errorBoddy}")
                    if (errorBoddy == null) {
                        Log.d(TAG+"_ERROR!!", "데이터 없음")
                        _toastMsgLiveData.postValue("ERROR: 데이터 없음: "+errorBoddy)
                        _carListLiveData.postValue(Resource.Retry())
                    }
                }
            }
        }
    }
}


//edit: 코틀린의 코루틴은 다음과 같은 주요 디스패처를 제공합니다:
//
//Dispatchers.Main: 주 스레드에서 작업을 수행합니다. UI 작업에 사용.
//Dispatchers.IO: 입출력 작업에 최적화된 백그라운드 스레드에서 작업을 수행.
//Dispatchers.Default: CPU 집약적인 작업에 최적화된 백그라운드 스레드에서 작업을 수행.