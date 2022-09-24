package com.harifrizki.storyapp.module.errorpage

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.databinding.ActivityConnectionErrorBinding
import com.harifrizki.storyapp.utils.*
import com.harifrizki.storyapp.utils.ErrorState.*

class ConnectionErrorActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityConnectionErrorBinding.inflate(layoutInflater)
    }

    private var errorState: ErrorState? = null
    private var generalResponse: GeneralResponse? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        errorState = intent?.getStringExtra(ERROR_STATE)?.let { ErrorState.valueOf(it) }
        when (errorState) {
            IS_NO_NETWORK -> {
                with(binding)
                {
                    tvTitleMessage.text = getString(R.string.message_no_title_network)
                    tvMessage.text = getString(R.string.message_no_network)
                }
            }
            IS_ERROR_RESPONSE_API -> {
                generalResponse =
                    if (checkBuildOS(Build.VERSION_CODES.TIRAMISU)) intent?.getParcelableExtra(
                        GENERAL_RESPONSE,
                        GeneralResponse::class.java
                    ) else @Suppress("DEPRECATION") intent?.getParcelableExtra(
                        GENERAL_RESPONSE
                    )

                if (resources.getBoolean(R.bool.app_debug_mode)) {
                    with(binding)
                    {

                        with(binding) {
                            tvTitleMessage.text = getString(R.string.message_error_title_general)
                            tvMessage.text = generalResponse?.message
                        }
                    }
                } else {
                    with(binding) {
                        tvTitleMessage.text = getString(R.string.message_error_title_general)
                        tvMessage.text = getString(R.string.message_error_general)
                    }
                }
            }
            else -> {}
        }
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}