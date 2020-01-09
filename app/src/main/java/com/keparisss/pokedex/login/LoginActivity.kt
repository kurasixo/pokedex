package com.keparisss.pokedex.login

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.keparisss.pokedex.R
import com.keparisss.pokedex.list.ListActivity
import com.keparisss.pokedex.signup.SignUpActivity
import com.keparisss.pokedex.signup.afterTextChanged
import kotlinx.coroutines.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private val fiveMins: Long = 5 * 60 * 1000
    private var debJob: Job? = null

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)

        val username = findViewById<EditText>(R.id.login_username)
        val password = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val loading = findViewById<ProgressBar>(R.id.login_loading)
        val forgotPasswordButton = findViewById<Button>(R.id.forgot_password_button)

        displayBiometricPrompt()

        loginViewModel = ViewModelProviders.of(this, AuthViewModelFactory())
            .get(LoginViewModel::class.java)

        val preferences: SharedPreferences =
            getSharedPreferences("sensitiveData", Context.MODE_PRIVATE)

        val lastLoggedInTime = preferences.getLong("lastAppOpened", 0.toLong())
        if (lastLoggedInTime != 0.toLong()) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastLoggedInTime < fiveMins) {
                openListActivity()
            }
        }

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }

            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE

            if (loginResult.success != null) {
                openListActivity()
            }

            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )

                debJob?.cancel()
                debJob = CoroutineScope(Dispatchers.IO).launch {
                    delay(150L)

                    loginViewModel.login(
                        username.text.toString(),
                        password.text.toString(),
                        preferences
                    )
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            preferences
                        )
                }
                false
            }

            loginButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString(),
                    preferences
                )
            }
        }

        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openListActivity() {
        val intent = Intent(this@LoginActivity, ListActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun displayBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    openListActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()


        biometricPrompt.authenticate(promptInfo)
    }
}
