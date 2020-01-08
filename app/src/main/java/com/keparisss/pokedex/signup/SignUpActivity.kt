package com.keparisss.pokedex.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.keparisss.pokedex.models.LoggedInUserView

import com.keparisss.pokedex.R
import com.keparisss.pokedex.list.ListActivity
import com.keparisss.pokedex.login.LoginViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var signupViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signup_activity)

        val username = findViewById<EditText>(R.id.signup_username)
        val password = findViewById<EditText>(R.id.signup_password)
        val login = findViewById<Button>(R.id.signup_button)
        val loading = findViewById<ProgressBar>(R.id.signup_loading)

        signupViewModel = ViewModelProviders.of(this,
            LoginViewModelFactory()
        )
            .get(SignUpViewModel::class.java)

        signupViewModel.loginFormState.observe(this@SignUpActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        signupViewModel.signupResult.observe(this@SignUpActivity, Observer {
            val signupResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (signupResult.error != null) {
                showLoginFailed(signupResult.error)
            }

            if (signupResult.success != null) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                updateUiWithUser(signupResult.success)

                finish()
            }

            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            signupViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            val preferences: SharedPreferences =
                getSharedPreferences("sensitiveData", Context.MODE_PRIVATE)

            afterTextChanged {
                signupViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signupViewModel.signup(
                            username.text.toString(),
                            password.text.toString(),
                            preferences
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                signupViewModel.signup(
                    username.text.toString(),
                    password.text.toString(),
                    preferences
                )
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}