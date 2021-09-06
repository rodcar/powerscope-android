package pe.powerscope.app.controllers.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.basgeekball.awesomevalidation.AwesomeValidation
import kotlinx.android.synthetic.main.activity_sign_up.*
import pe.powerscope.app.network.requests.SignUpRequest
import pe.powerscope.app.network.responses.SignUpResponse
import pe.powerscope.app.R
import pe.powerscope.app.network.ApiService
import pe.powerscope.app.network.requests.SignInRequest
import pe.powerscope.app.network.responses.SignInResponse
import pe.powerscope.app.util.LoadingUtil
import pe.powerscope.app.util.LogUtil
import pe.powerscope.app.util.NavigationUtil
import pe.powerscope.app.util.UserUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        LoadingUtil.progressBar = progressBarSignUpWaiting

        buttonSignUpSignUp.setOnClickListener {
            signUp(applicationContext)
        }
    }

    private fun signUp(context: Context) {
        if (LoadingUtil.isDisplayed) {
            return
        }

        var request = SignUpRequest()
        request.email = editTextSignUpEmail.text.toString()
        request.password = editTextSignUpPassword.text.toString()
        request.name = editTextSignUpName.text.toString()
        request.lastName = editTextSignUpLastName.text.toString()

        LoadingUtil.show()
        ApiService.instance.signUp(request)
            .enqueue(object : Callback<SignUpResponse> {
                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    LoadingUtil.hide()
                    LogUtil.e(TAG, t)
                }

                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    Log.i(TAG, response.code().toString())

                    when (response.code()) {
                        200 -> {
                            Log.i(TAG, response.code().toString())
                            ApiService.instance.signIn(
                                SignInRequest(
                                    request.email,
                                    request.password
                                )
                            ).enqueue(object : Callback<SignInResponse> {
                                    override fun onFailure(
                                        call: Call<SignInResponse>,
                                        t: Throwable
                                    ) {
                                        LogUtil.e(TAG, t)
                                    }

                                    override fun onResponse(
                                        call: Call<SignInResponse>,
                                        response: Response<SignInResponse>
                                    ) {
                                        when (response.code()) {
                                            200 -> {
                                                Log.i(TAG, response.code().toString())
                                                LoadingUtil.hide()
                                                response.body()?.apply {
                                                    UserUtil.authenticate(context, id, accessToken)
                                                }
                                                val intent = Intent(context, HomeActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                startActivity(intent)
                                            }
                                            else -> {

                                            }
                                        }
                                    }

                                })
                        }
                        else -> {

                        }
                    }
                }
            })
    }
}