package com.xinzy.compose.wan.http

import com.xinzy.compose.wan.entity.ApiResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class SuspendCallAdapterFactory : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        fun create(): SuspendCallAdapterFactory = SuspendCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType == HttpResult::class.java) {
            throw IllegalArgumentException("Method must be declare suspend, please check function declaration at API interface")
        }

        //协程挂起函数默认返回值是Call<*>，如果不满足该条件，那么返回null让retrofit选择其他家伙来Py
        if (Call::class.java != rawType) {
            return null
        }

        //检查Call内部的泛型是否包含了其他泛型
        check(returnType is ParameterizedType) { "return type must be ApiResult<*> or ApiResult<out *> for Call<*> check" }

        //获取Call类包裹的第一个泛型
        val responseType = getParameterUpperBound(0, returnType)

        //Call类包裹的第一个泛型不是HttpResult类，那么返回null，让retrofit选择其他 CallAdapter.Factory
        if (getRawType(responseType) != HttpResult::class.java) {
            return null
        }

        //确保HttpResult内部包的泛型其还包裹另外一层泛型，比如 HttpResult<*>
        check(responseType is ParameterizedType) { "return type must be HttpResult<*> or HttpResult<out *> for HttpResult<*> check" }

        //获取HttpResult类包裹的第一个泛型
        val successBodyType = getParameterUpperBound(0, responseType)

        return SuspendCallAdapter<ApiResult<Any>, Any>(successBodyType)
    }

    class SuspendCallAdapter<T : ApiResult<R>, R : Any>(
        private val successType: Type
    ) : CallAdapter<T, Call<HttpResult<ApiResult<R>>>> {
        override fun responseType(): Type = successType

        override fun adapt(call: Call<T>): Call<HttpResult<ApiResult<R>>> = SuspendCall(call)
    }

    class SuspendCall<T : ApiResult<R>, R : Any>(
        private val delegate: Call<T>
    ) : Call<HttpResult<ApiResult<R>>> {

        override fun clone(): Call<HttpResult<ApiResult<R>>> = SuspendCall(delegate.clone())

        override fun execute(): Response<HttpResult<ApiResult<R>>> {
            throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
        }

        override fun isExecuted(): Boolean = delegate.isExecuted

        override fun cancel() = delegate.cancel()

        override fun isCanceled(): Boolean = delegate.isCanceled

        override fun request(): Request = delegate.request()

        override fun timeout(): Timeout = delegate.timeout()

        override fun enqueue(callback: Callback<HttpResult<ApiResult<R>>>) {
            delegate.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()

                    if (response.isSuccessful) {
                        var apiResult: HttpResult<ApiResult<R>>? = if (body != null) {
                            if (body.isSuccess) HttpResult.Success(body)
                            else HttpResult.Failure(body.code, body.message)
                        } else {
                            HttpResult.Failure(1, "接口错误")
                        }

                        callback.onResponse(this@SuspendCall, Response.success(apiResult))
                    } else {
                        onFailure(call, HttpException(response))
                    }
                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    throwable.printStackTrace()
                    val result = HttpResult.Failure(2, "网络错误")
                    callback.onResponse(this@SuspendCall, Response.success(result))
                }
            })
        }
    }
}