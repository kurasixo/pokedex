package com.keparisss.pokedex.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

import com.keparisss.pokedex.R
import com.keparisss.pokedex.list.ListActivity
import com.keparisss.pokedex.login.AuthViewModelFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var signupViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signup_activity)

        val username = findViewById<EditText>(R.id.signup_username)
        val password = findViewById<EditText>(R.id.signup_password)
        val signupButton = findViewById<Button>(R.id.signup_button)
        val loading = findViewById<ProgressBar>(R.id.signup_loading)

        val preferences: SharedPreferences =
            getSharedPreferences("sensitiveData", Context.MODE_PRIVATE)

        signupViewModel = ViewModelProviders.of(this, AuthViewModelFactory())
            .get(SignUpViewModel::class.java)

        signupViewModel.loginFormState.observe(this@SignUpActivity, Observer {
            val loginState = it ?: return@Observer

            signupButton.isEnabled = loginState.isDataValid

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
            if (signupResult.success != null) {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
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

            signupButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                signupViewModel.signup(
                    username.text.toString(),
                    password.text.toString(),
                    preferences
                )
            }
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
