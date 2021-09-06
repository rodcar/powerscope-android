package pe.powerscope.app.controllers.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationHolder
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation
import com.google.gson.Gson
import pe.powerscope.app.R
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_sign_in.*
import pe.powerscope.app.network.ApiService
import pe.powerscope.app.network.requests.SignInRequest
import pe.powerscope.app.network.responses.ErrorResponse
import pe.powerscope.app.network.responses.SignInResponse
import pe.powerscope.app.util.ResizeAnimation
import ppe.powerscope.app.util.ValidationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    private lateinit var awesomeValidation: AwesomeValidation
    private var isLoading: Boolean = false
    private var isSignInFormDisplay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Events
        buttonSignInSignIn.setOnClickListener {
            if (isSignInFormDisplay) {
                signIn()
            } else {
                val resizeAnimation = ResizeAnimation(layoutSignInForm, 530, 0)
                resizeAnimation.duration = 400
                layoutSignInForm.startAnimation(resizeAnimation)
                buttonSignInSignIn.text = "Ingresar"
                isSignInFormDisplay = true
            }
        }

        textViewSignInSignUp.setOnClickListener { goToSignUp() }

        editTextSignInPassword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) signIn()
            false
        }

        editTextSignInEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextSignInPassword.setError(null);
            }

        })

        textViewSignInPasswordRecoveryText.setOnClickListener { goToPasswordRecovery() }

        // Validations
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)

        /*awesomeValidation.addValidation(
            editTextSignInEmail, android.util.Patterns.EMAIL_ADDRESS, getString(
                R.string.string_edittext_email_validation_invalid_format
            )
        )*/

        awesomeValidation.addValidation(editTextSignInPassword, SimpleCustomValidation {
            val password = it.trim()
            password.length in 8..50
        }, "Ingrese una contraseña válida")
        awesomeValidation.addValidation(
            editTextSignInPassword,
            ValidationUtil.EmptyValidation,
            "Ingrese una contraseña"
        )
        /*awesomeValidation.addValidation(editTextSignInPassword, SimpleCustomValidation {
            val specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            val hasSpecialMatcher = specialCharPattern.matcher(editTextSignInPassword.text)
            editTextSignInPassword.length() >= 8 && hasSpecialMatcher.find()
        }, getString(R.string.string_edittext_password_validation_invalid_password))*/

        awesomeValidation.addValidation(
            editTextSignInEmail,
            object : CustomValidation {
                override fun compare(holder: ValidationHolder?): Boolean {
                    val editTextEmail = holder?.view as EditText
                    return android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString())
                        .matches() or (Pattern.matches(
                        "[0-9]+",
                        editTextEmail.text
                    ) and (editTextEmail.text.length == 9))
                }
            },
            object : CustomValidationCallback {
                override fun execute(holder: ValidationHolder?) {
                    val imm =
                        applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(holder?.view?.windowToken, 0)
                    editTextSignInEmail.requestFocus()
                    editTextSignInEmail.setError(getString(R.string.string_edittext_email_validation_invalid_format))
                }
            },
            object : CustomErrorReset {
                override fun reset(p0: ValidationHolder?) {

                }
            }, getString(R.string.string_edittext_email_validation_invalid_format)
        )

        awesomeValidation.addValidation(
            editTextSignInEmail,
            ValidationUtil.EmptyValidation,
            "Ingrese un correo electrónico o número de celular"
        )
    }

    fun signIn() {
        if (!awesomeValidation.validate() || isLoading) {
            return
        }
        showLoading()

        val email = editTextSignInEmail.text.toString()
        val password = editTextSignInPassword.text.toString()

        ApiService.instance.signIn(SignInRequest(email, password))
            .enqueue(object : Callback<SignInResponse> {
                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            val userToken = response.body()?.accessToken
                            val userId = response.body()?.id!!

                            val sharedPref = applicationContext?.getSharedPreferences(
                                getString(R.string.preference_file_key),
                                Context.MODE_PRIVATE
                            )!!

                            with(sharedPref.edit()) {
                                putString(getString(R.string.saved_user_token_key), userToken)
                                putInt(getString(R.string.saved_user_id_key), userId)
                                commit()
                            }

                            goToHome()
                        }
                        400 -> {
                            val gson = Gson()
                            val errorResponse = gson.fromJson(
                                response.errorBody()?.charStream(),
                                ErrorResponse::class.java
                            )

                            val message = errorResponse.message!!

                            if (message.contains("Validation failed for object='loginForm'")) {
                                editTextSignInEmail.requestFocus()
                                editTextSignInEmail.setError(getString(R.string.string_edittext_email_validation_invalid_format))
                            }
                            hideLoading()
                        }
                        401 -> {
                            val gson = Gson()
                            val errorResponse = gson.fromJson(
                                response.errorBody()?.charStream(),
                                ErrorResponse::class.java
                            )
                            Log.i("SIGNIN", response.errorBody().toString());
                            val message = errorResponse.message
                            if (message.equals("Error -> The email is not registered")) {
                                editTextSignInEmail.requestFocus()
                                if (Pattern.matches("[0-9]+", email) and (email.length == 9)) {
                                    editTextSignInEmail.setError("El número de celular no está registrado")
                                } else {
                                    editTextSignInEmail.setError("El correo electrónico no está registrado")
                                }
                            } else if (message.equals("Error -> The password is not correct")) {
                                editTextSignInPassword.requestFocus()
                                editTextSignInPassword.setError("La contraseña es incorrecta")
                            }
                            hideLoading()
                        }
                        else -> {
                            hideLoading()
                            Log.i("SIGNIN", response.code().toString());
                        }
                    }
                }
            })
    }

    fun goToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun goToPasswordRecovery() {
    }

    fun showLoading() {
        isLoading = true
        progressBarSignInWaiting.visibility = View.VISIBLE
    }

    fun hideLoading() {
        isLoading = false
        progressBarSignInWaiting.visibility = View.GONE
    }
}