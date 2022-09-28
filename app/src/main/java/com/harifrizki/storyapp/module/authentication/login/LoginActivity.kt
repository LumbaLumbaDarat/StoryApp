package com.harifrizki.storyapp.module.authentication.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.databinding.ActivityLoginBinding
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.module.authentication.registration.RegistrationActivity
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.story.liststory.ListStoryActivity
import com.harifrizki.storyapp.utils.DataResource
import com.harifrizki.storyapp.utils.MODEL_LOGIN
import com.harifrizki.storyapp.utils.PreferencesManager
import com.harifrizki.storyapp.utils.makeSpannable
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.harifrizki.storyapp.utils.ResponseStatus.*

class LoginActivity : BaseActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
            createAppBar(
                appBar,
                title = getString(R.string.app_name),
                subTitle = getString(R.string.app_sub)
            )
            tvRegistration.apply {
                text = makeSpannable(text = getString(R.string.message_registration), onClicked = {
                    goToRegistration()
                })
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = Color.TRANSPARENT
            }
            btnLogin.setOnClickListener {
                validateLogin()
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    private fun login(login: Login?) {
        if (networkConnected()) {
            viewModel.login(login).observe(this, this.login)
        }
    }

    private val login = Observer<DataResource<LoginResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(getString(R.string.message_loading_registration))
            }
            SUCCESS -> {
                dismissLoading()
                when (isResponseSuccess(GeneralResponse(it.data?.error, it.data?.message))) {
                    true -> goToDashboard(it.data)
                    false -> resetEditText(
                        arrayOf(
                            binding.edLoginEmail,
                            binding.edLoginPassword
                        )
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                wasError(it.generalResponse)
            }
            else -> {}
        }
    }

    private fun validateLogin() {
        if (binding.edLoginEmail.valid()) {
            if (binding.edLoginPassword.valid()) {
                login(
                    Login(
                        email = binding.edLoginEmail.text.toString().trim(),
                        password = binding.edLoginPassword.text.toString().trim()
                    )
                )
            }
        }
    }

    private fun goToRegistration() {
        Intent(this, RegistrationActivity::class.java).apply {
            resultLauncher.launch(this)
        }
    }

    private fun goToDashboard(loginResponse: LoginResponse?) {
        PreferencesManager.getInstance(this).apply {
            setPreferences(
                MODEL_LOGIN,
                loginResponse?.loginResult
            )
        }
        Intent(this, ListStoryActivity::class.java).apply {
            resultLauncher.launch(this)
        }
    }
}