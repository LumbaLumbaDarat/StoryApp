package com.harifrizki.storyapp.module.camerapage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityCameraBinding
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.IS_BACK_FROM_CAMERA
import com.harifrizki.storyapp.utils.RESULT_CAPTURE_IMAGE
import com.harifrizki.storyapp.utils.WAS_SUCCESS_GET_IMAGE
import com.harifrizki.storyapp.utils.createFile
import java.util.concurrent.Executors

class CameraActivity : BaseActivity() {

    private val binding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }
    private val cameraExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    private var cameraSelector: CameraSelector? = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var result: Intent = Intent()
    private var successGetImage: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        binding.ivCaptureCamera.setOnClickListener(onClickListener)
        binding.ivSwitchCamera.setOnClickListener(onClickListener)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                result.putExtra(WAS_SUCCESS_GET_IMAGE, successGetImage)
                setResult(RESULT_OK, result)
                finish()
            }
        }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        when (it.id) {
            R.id.ivSwitchCamera -> {
                cameraSelector =
                    if (cameraSelector?.equals(CameraSelector.DEFAULT_BACK_CAMERA) == true) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }
            R.id.ivCaptureCamera -> {
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showError(
                        message = getString(R.string.message_failed_capture_image),
                        onClick = {
                            onResume()
                        })
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    result.putExtra(RESULT_CAPTURE_IMAGE, photoFile)
                    result.putExtra(
                        IS_BACK_FROM_CAMERA,
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    successGetImage = true
                    onBackPressedCallback.handleOnBackPressed()
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraSelector?.let {
                    cameraProvider.bindToLifecycle(
                        this,
                        it,
                        preview,
                        imageCapture
                    )
                }
            } catch (e: Exception) {
                showError(
                    titleNotification = getString(R.string.message_failed_show_camera),
                    message = e.message,
                    onClick = {
                        result.putExtra(
                            IS_BACK_FROM_CAMERA,
                            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                        )
                        successGetImage = false
                        onBackPressedDispatcher.onBackPressed()
                    })
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        else window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }
}