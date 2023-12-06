package com.xinzy.compose.wan.http

import com.xinzy.compose.wan.entity.ApiResult
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.WanList
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface WanApi {

    /** 置顶 */
    @GET("/article/top/json")
    suspend fun topArticle(): HttpResult<ApiResult<List<Article>>>

    /** 首页banner */
    @GET("/banner/json")
    suspend fun banner(): HttpResult<ApiResult<List<Banner>>>

    /** 首页文章列表 */
    @GET("/article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): HttpResult<ApiResult<WanList<Article>>>

    /** 体系数据 */
    @GET("/tree/json")
    suspend fun chapter(): HttpResult<ApiResult<List<Chapter>>>

    companion object {
        private var api: WanApi? = null

        fun api(): WanApi {
            return api ?: kotlin.run {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .sslSocketFactory(createSSLSocketFactory(), TrustAllManager())
                    .hostnameVerifier(TrustHostnameVerifier())
                    .build()

                Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(SuspendCallAdapterFactory.create())
                    .build()
                    .create(WanApi::class.java)
                    .apply { api = this }
            }
        }

        private fun createSSLSocketFactory(): SSLSocketFactory {
            return try {
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, arrayOf<TrustManager>(TrustAllManager()), SecureRandom())
                sc.socketFactory
            } catch (e: Exception) {
                throw RuntimeException("create ssl socket factory fail")
            }
        }
    }


    private class TrustHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean = true
    }

    private class TrustAllManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }
}