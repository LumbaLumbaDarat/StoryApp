package com.harifrizki.storyapp.module.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.databinding.ActivitySplashScreenBinding
import com.harifrizki.storyapp.module.authentication.login.LoginActivity
import com.harifrizki.storyapp.module.story.liststory.ListStoryActivity
import com.harifrizki.storyapp.utils.MODEL_LOGIN
import com.harifrizki.storyapp.utils.PreferencesManager
import com.harifrizki.storyapp.utils.WAIT_FOR_2000
import com.harifrizki.storyapp.utils.getVersion

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val userLogin: LoginResultResponse? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(MODEL_LOGIN, LoginResultResponse::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvAppsVersion.text = getString(
            R.string.app_version,
            getVersion(this@SplashScreenActivity)
        )
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = if (userLogin == null)
                Intent(
                    this,
                    LoginActivity::class.java
                )
            else Intent(
                this,
                ListStoryActivity::class.java
            )
            startActivity(intent)
            finish()
        }, WAIT_FOR_2000)
    }
}