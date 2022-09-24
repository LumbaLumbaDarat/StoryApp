package com.harifrizki.storyapp.module.authentication.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityLoginBinding
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.module.authentication.registration.RegistrationActivity
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.story.liststory.ListStoryActivity
import com.harifrizki.storyapp.utils.makeSpannable

class LoginActivity : BaseActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        binding.apply {
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
            goToDashboard()
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

    private fun goToDashboard() {
        Intent(this, ListStoryActivity::class.java).apply {
            startActivity(this)
        }
    }
}