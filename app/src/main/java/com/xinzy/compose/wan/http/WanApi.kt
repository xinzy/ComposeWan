package com.xinzy.compose.wan.http

import android.annotation.SuppressLint
import com.xinzy.compose.wan.WanApplication
import com.xinzy.compose.wan.entity.ApiResult
import com.xinzy.compose.wan.entity.Article
import com.xinzy.compose.wan.entity.Banner
import com.xinzy.compose.wan.entity.Chapter
import com.xinzy.compose.wan.entity.Navigation
import com.xinzy.compose.wan.entity.Rank
import com.xinzy.compose.wan.entity.Score
import com.xinzy.compose.wan.entity.ScoreRecord
import com.xinzy.compose.wan.entity.User
import com.xinzy.compose.wan.entity.WanList
import com.xinzy.compose.wan.util.L
import com.xinzy.compose.wan.util.md5
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
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

    /** 导航数据 */
    @GET("/navi/json")
    suspend fun navigation(): HttpResult<ApiResult<List<Navigation>>>

    /** 知识体系下的文章列表  */
    @GET("/article/list/{page}/json")
    suspend fun topicByChapterId(@Path("page") page: Int, @Query("cid") cid: Int)
            : HttpResult<ApiResult<WanList<Article>>>

    /** 微信公众号列表  */
    @GET("/wxarticle/chapters/json")
    suspend fun weixin(): HttpResult<ApiResult<List<Chapter>>>

    /** 知识体系下的文章列表  */
    @GET("/wxarticle/list/{cid}/{page}/json")
    suspend fun wechatArticleList(@Path("page") page: Int, @Path("cid") cid: Int,
                      @Query("k") keyword: String?): HttpResult<ApiResult<WanList<Article>>>

    /** 项目分类列表  */
    @GET("/project/tree/json")
    suspend fun project(): HttpResult<ApiResult<List<Chapter>>>

    /** 项目分类下的文章列表 */
    @GET("/project/list/{page}/json")
    suspend fun projectArticleList(@Path("page") page: Int, @Query("cid") cid: Int)
            : HttpResult<ApiResult<WanList<Article>>>



    //////////////////////////////////////////////////////////////////////////////////////////
    // 用户模块
    //////////////////////////////////////////////////////////////////////////////////////////

    /** 登录 */
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): HttpResult<ApiResult<User>>

    /**
     * 用户登录
     */
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String, @Field("password") password: String,
                 @Field("repassword") repassword: String): HttpResult<ApiResult<User>>


    /**
     * 退出
     */
    @GET("/user/logout/json")
    suspend fun logout(): HttpResult<ApiResult<Any>>

    /**
     * 我的积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun coin(): HttpResult<ApiResult<Score>>

    /**
     * 积分记录
     */
    @GET("/lg/coin/list/{page}/json")
    suspend fun coinList(@Path("page") page: Int): HttpResult<ApiResult<WanList<ScoreRecord>>>

    /**
     * 积分排行
     */
    @GET("/coin/rank/{page}/json")
    suspend fun rank(@Path("page") page: Int): HttpResult<ApiResult<WanList<Rank>>>


    companion object {
        private var api: WanApi? = null

        fun api(): WanApi {
            return api ?: kotlin.run {
                val logging = HttpLoggingInterceptor(WanLogger())
                logging.level = HttpLoggingInterceptor.Level.BASIC

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .cookieJar(PersistentCookieJar())
                    .sslSocketFactory(createSSLSocketFactory(), TrustAllManager())
                    .hostnameVerifier(TrustHostnameVerifier())
                    .addNetworkInterceptor(logging)
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
        @SuppressLint("BadHostnameVerifier")
        override fun verify(hostname: String?, session: SSLSession?): Boolean = true
    }

    @SuppressLint("CustomX509TrustManager")
    private class TrustAllManager : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }

    private class WanLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            L.v(message)
        }
    }

    private class PersistentCookieJar : CookieJar {
        private val cookieDir: File = File(WanApplication.getInstance().filesDir, "cookie")

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val dir = getCookieDir(url)
            cookies.forEach {
                if (it.persistent) {
                    val name = it.name
                    val file = File(dir, name)
                    if (isExpires(it)) {
                        if (file.exists()) file.delete()
                    } else {
                        val content = "${it.value}|${it.domain}|${it.path}|${it.expiresAt}|${it.hostOnly}|${it.httpOnly}"
                        runCatching {
                            file.writeText(content)
                        }
                    }
                }
            }
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            val list = mutableListOf<Cookie>()
            val dir = getCookieDir(url)

            dir.listFiles()?.forEach { file ->
                file.readLines().filter {
                    it.isNotEmpty()
                }.map {
                    it.split("|")
                }.first {
                    it.size == 6
                }.let {
                    val value = it[0]
                    val domain = it[1]
                    val path = it[2]
                    val expiresAt = it[3].toLong()
                    val hostOnly = it[4].toBoolean()
                    val httpOnly = it[5].toBoolean()
                    if (expiresAt > System.currentTimeMillis()) {
                        val builder = Cookie.Builder()
                            .name(file.name)
                            .value(value)
                            .domain(domain)
                            .path(path)
                            .expiresAt(expiresAt)
                        if (hostOnly) builder.hostOnlyDomain(domain)
                        if (httpOnly) builder.httpOnly()

                        val cookie = builder.build()
                        if (cookie.matches(url)) {
                            list.add(cookie)
                        }
                    }
                }
            }
            return list
        }

        fun isExpires(cookie: Cookie) = System.currentTimeMillis() > cookie.expiresAt

        private fun getCookieDir(url: HttpUrl): File {
            val filename = url.host.md5()
            return File(cookieDir, filename).apply { if (!exists()) mkdirs() }
        }
    }

}