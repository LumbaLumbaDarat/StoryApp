package com.harifrizki.storyapp.module.authentication.registration

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.databinding.ActivityRegistrationBinding
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.DataResource
import com.harifrizki.storyapp.utils.ResponseStatus.*
import com.harifrizki.storyapp.utils.WAS_REGISTRATION
import com.harifrizki.storyapp.utils.makeSpannable
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : BaseActivity() {
    private val binding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<RegistrationViewModel>()

    private var wasRegistration: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
            createAppBar(
                appBar,
                title = getString(R.string.app_name),
                subTitle = getString(R.string.registration_sub)
            )
            tvLogin.apply {
                text = makeSpannable(text = getString(R.string.message_login)) {
                    onBackPressedDispatcher.onBackPressed()
                }
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = Color.TRANSPARENT
            }
            edRegisterName.name(getString(R.string.name))
            btnRegistration.setOnClickListener { validateRegistration() }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Intent().apply {
                    putExtra(WAS_REGISTRATION, wasRegistration)
                    setResult(RESULT_OK, this)
                }
                finish()
            }
        }

    private fun registration(registration: Registration?) {
        if (networkConnected()) {
            viewModel.registration(registration).observe(this, this.registration)
        }
    }

    private val registration = Observer<DataResource<GeneralResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(getString(R.string.message_loading_registration))
            }
            SUCCESS -> {
                dismissLoading()
                when (isResponseSuccess(it.data)) {
                    true -> successRegistration()
                    false -> resetEditText(
                        arrayOf(
                            binding.edRegisterName,
                            binding.edRegisterEmail,
                            binding.edRegisterPassword
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

    private fun validateRegistration() {
        if (binding.edRegisterName.valid()) {
            if (binding.edRegisterEmail.valid()) {
                if (binding.edRegisterPassword.valid()) {
                    registration(
                        Registration(
                            name = binding.edRegisterName.text.toString().trim(),
                            email = binding.edRegisterEmail.text.toString().trim(),
                            password = binding.edRegisterPassword.text.toString().trim()
                        )
                    )
                }
            }
        }
    }

    private fun successRegistration() {
        showSuccess(
            message = getString(R.string.message_success_registration),
            onClick = {
                dismissNotification()
                wasRegistration = true
                onBackPressedDispatcher.onBackPressed()
            }
        )
    }
}