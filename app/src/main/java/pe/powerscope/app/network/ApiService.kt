package pe.powerscope.app.network

import okhttp3.OkHttpClient
import pe.chiararodriguez.amelia.network.requests.SignInRequest
import pe.chiararodriguez.amelia.network.requests.SignUpRequest
import pe.chiararodriguez.amelia.network.responses.SignInResponse
import pe.chiararodriguez.amelia.network.responses.SignUpResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface ApiService {

    companion object {
        const val HEADER_AUTH = "Authorization"
        const val API_BASE_URL = "https://powerscope-api.herokuapp.com" 

        val instance: ApiService by lazy {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create<ApiService>(ApiService::class.java)
        }

        fun signIn(email: String, password: String): Call<SignInResponse> {
            return this.instance.signIn(SignInRequest(email, password))
        }

        /*fun signUp(email: String, password: String): Call<SignUpResponse> {
            return this.instance.signUp(SignUpRequest(email, password))
        }*/
    }

    // Authentication

    @POST("/auth/signin")
    fun signIn(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @POST("/auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>
}