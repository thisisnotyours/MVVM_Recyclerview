package com.thisisnotyours.mvvm_recyclerview.response

import com.google.gson.annotations.SerializedName

class LoginResponse {
    var status: String? = null
    var data: Data? = null
    @SerializedName("message")
    var msg: String? = null

    override fun toString(): String {
        return "LoginResponse(status=$status, data=$data, message=$msg)"
    }

    class Data {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("role")
        var role: Int? = null

        @SerializedName("city_id")
        var cityId: Int? = null

        @SerializedName("city_name")
        var cityName: String? = null

        override fun toString(): String {
            return "Data(id=$id, name=$name, role=$role, city_id=$cityId, city_name=$cityName)"
        }
    }
}


//edit: ?
// ? 는 nullable 이라는 의미를 가집니다.
// 변수의 타입 뒤에 ? 를 붙이면 해당 변수가 null 일 수도 있는 경우에 대해 컴파일러가 경고를 내지 않습니다.

//예를 들어, var status: String? = null 은
//status 변수가 String 타입인데 null 일 수도 있는 상황을 허용하겠다는 의미입니다.
// 이 변수를 사용할 때 null 일 수도 있기 때문에, null 체크를 하지 않으면 NullPointerException 오류가 발생할 수 있습니다.
// 따라서, ? 를 사용하여 nullable 타입을 정의할 때는 null 체크를 잊지 않도록 주의해야 합니다.