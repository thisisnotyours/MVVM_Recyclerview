package com.thisisnotyours.mvvm_recyclerview.response

import okhttp3.Headers

sealed class Resource<T>(
    val data: T? = null,
    val exception: Throwable? = null,
    val headers: Headers? = null
) {
    class Success<T>(data: T, headers: Headers? = null) : Resource<T>(data, null, headers)
    class Error<T>(exception: Throwable) : Resource<T>(null, exception)
    class Loading<T> : Resource<T>()
    class Retry<T> : Resource<T>()
}




//edit: sealed class
//sealed 키워드는 상속 가능한 클래스 계층을 제한하는데 사용됩니다.
//즉, sealed 클래스는 특정한 상속 클래스들의 계층을 묶어서 sealed 클래스 내부에서만 상속을 허용하고 외부에서는 상속을 불가능하게 합니다.

//edit: <out T>
//타입 매개변수 T가 출력전용(producer)타입 매개변수를 나타냄.
//이는 타입이 함수의 반환 값으로만 사용되고, 함수 내에서 수정되지 않는다는 것을 보장함.

//edit: <T>
//<T>는 타입 매개변수 T를 선언하는데, out 이나 in 키워드가 없으면 기본적으로 일반적인 타입 매개변수로 선언됨.
//즉, 해당타입이 입력(input)과 출력(output)모두 가능하다는 것을 의미함.

//edit: <Nothing>
//Nothing 은 Kotlin 에서 모든 타입의 하위 타입이며, 어떠한 값을 가지지 않는 타입임.
//주로 함수에서 '예외'를 던질 때 사용되며, 반환타입으로 사용되면 함수가 항상 예외를 던진다는 것을 나타냄.

//edit: data class
//data class는 Kotlin에서 데이터를 다루기 위한 클래스로,
//여러 개의 프로퍼티를 가지며 equals(), hashCode(), toString(), copy() 등의 메서드가 자동으로 생성됩니다.
//위 코드에서 Success는 Resource 클래스의 '내부 클래스'로,
//data 프로퍼티를 가지고 있습니다. Success 클래스는 제네릭 타입 T를 받아들이고,
//data 프로퍼티는 T 타입의 객체를 가지며, Resource.Success 객체의 생성 시 해당 T 타입의 객체가 생성자를 통해 전달됩니다.





//edit: the most import thing about 'parameter 매개변수':
//Success 클래스는 제네릭 클래스로, 타입 매개변수 T를 사용하여 어떤 타입의 데이터든 처리할 수 있습니다.
//Success 클래스의 생성자는 두 개의 매개변수를 가집니다.
//data: T: 성공적으로 가져온 데이터를 나타냅니다. T 타입의 매개변수로 어떤 타입의 데이터든 저장할 수 있습니다.
//headers: Headers? = null: 성공적인 응답에 포함된 헤더를 나타냅니다. Headers 타입의 매개변수이며, 기본 값은 null입니다. 이 매개변수는 필요에 따라 사용하거나 생략할 수 있습니다.
//Resource<T>(data, null, headers)는 Resource 클래스의 생성자를 호출하고 있습니다. 여기서는 data와 headers를 전달하고, exception에는 null 값을 전달하여 오류가 없음을 나타냅니다.
//결론적으로, Success 클래스는 성공적인 API 요청의 결과를 나타내며, 가져온 데이터와 응답 헤더를 저장할 수 있습니다.