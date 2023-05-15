package com.thisisnotyours.mvvm_recyclerview.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.thisisnotyours.mvvm_recyclerview.repository.LoginRepository
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.IllegalArgumentException

class LoginViewModelFactory(private val loginRepository: LoginRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    private val TAG = "LoginViewModel"

    private val idLiveData = MutableLiveData<String>("")
    private val passwordLiveData = MutableLiveData<String>("")

    private val _toastLiveData = MutableLiveData<String>()
    val toastLiveData: LiveData<String>
        get() = _toastLiveData

    private val _loginSuccessLiveData = MutableLiveData<Boolean>(false)
    val loginSuccessLiveData: LiveData<Boolean>
        get() = _loginSuccessLiveData

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String>
        get() = _tokenLiveData

    fun setId(id:String) {
        idLiveData.value = id
    }

    fun getId(): LiveData<String> {
        return idLiveData
    }

    fun setPassword(password: String) {
        passwordLiveData.value = password
    }

    fun getPassword(): LiveData<String> {
        return passwordLiveData
    }

    fun performLogin(id: String, password: String) {
        setId(id)
        setPassword(password)
        isLoginValid()
    }

    fun isLoginValid(): Boolean {
        val id = idLiveData.value
        val password = passwordLiveData.value
        Log.d(TAG, "get login account : $id/ $password")

        if (id.isNullOrEmpty()) {
            _toastLiveData.postValue("Please type your 'ID'")
            return false
        }

        if (password.isNullOrEmpty()) {
            _toastLiveData.postValue("Please type your 'Password'")
            return false
        }

        viewModelScope.launch {
            val jsonObject = JsonObject().apply {
                addProperty("id", id)
                addProperty("password", password)
            }

            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())

            val response = loginRepository.getLoginData(requestBody)

            when (response) {
                is Resource.Success -> {
                    Log.d(TAG, "Login Success: ${response.data}")

                    val headers: Headers? = response.headers
                    val token = headers?.get("Set-Cookie")
                    _tokenLiveData.postValue(token?: "")
                    Log.d(TAG+"_token", token.toString())

                    if (response.data?.status == "success") {
                        val id = response.data?.data?.id
                        val name = response.data?.data?.name
                        val role = response.data?.data?.role
                        val cityId = response.data?.data?.cityId
                        val cityName = response.data?.data?.cityName

                        _loginSuccessLiveData.postValue(true)
                        _toastLiveData.postValue("login success")
                    }else{
                        _loginSuccessLiveData.postValue(true)
                        _toastLiveData.postValue("login fail: ${response.exception?.message}")
                    }
                }

                is Resource.Error -> {
                    Log.e(TAG, "Login Error: ${response.exception?.message}")
                    val errorBody = (response.exception as? HttpException)?.response()?.errorBody()?.string()
                    Log.d(TAG, "Login Error Body: ${errorBody}")
                    _loginSuccessLiveData.postValue(false)
                    _toastLiveData.postValue("login fail ${response.exception?.message}")
                }
            }
        }


        return _loginSuccessLiveData.value?: false
    }
}