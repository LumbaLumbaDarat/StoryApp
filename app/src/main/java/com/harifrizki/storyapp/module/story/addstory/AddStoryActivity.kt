package com.harifrizki.storyapp.module.story.addstory

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.databinding.ActivityAddStoryBinding
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.camerapage.CameraActivity
import com.harifrizki.storyapp.utils.*
import com.harifrizki.storyapp.utils.MenuCode.*
import com.harifrizki.storyapp.utils.ResponseStatus.*
import com.lumbalumbadrt.colortoast.ColorToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : BaseActivity() {
    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }
    private val userLogin: LoginResultResponse? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(MODEL_LOGIN, LoginResultResponse::class.java)
    }

    private val viewModel by viewModel<AddStoryViewModel>()
    private var menuCode: MenuCode = MENU_NONE
    private var imageFile: File? = null
    private var wasSuccessAddStory: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
            createAppBar(
                appBar,
                title = getString(R.string.app_name),
                subTitle = getString(R.string.sub_add_image)
            )
            tvMessageAddImage.text =
                makeSpannable(true, text = getString(R.string.message_add_image))
            ivAddImage.setOnClickListener(onClickListener)
            btnAdd.setOnClickListener(onClickListener)
            btnCancel.setOnClickListener(onClickListener)
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionsGranted()) {
            showWarning(
                titleNotification = getString(R.string.title_failed_get_permission),
                message = getString(R.string.message_failed_get_permission),
                onClick = {
                    onBackPressedDispatcher.onBackPressed()
                }
            )
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == RESULT_OK) {
            when (menuCode) {
                MENU_CAMERA -> {
                    if (it.data?.getBooleanExtra(WAS_SUCCESS_GET_IMAGE, false) == true) {
                        showLoading(getString(R.string.message_loading_convert_image))
                        Handler(Looper.getMainLooper()).postDelayed({
                            @Suppress("DEPRECATION")
                            imageFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                                it.data?.getSerializableExtra(RESULT_CAPTURE_IMAGE, File::class.java)
                            else it.data?.getSerializableExtra(RESULT_CAPTURE_IMAGE) as File
                            val isBackFromCamera = it.data?.getBooleanExtra(IS_BACK_FROM_CAMERA, true)
                            try {
                                doGlide(
                                    this,
                                    binding.ivAddImage,
                                    BitmapFactory.decodeFile(imageFile?.path),
                                    scaleType = ImageView.ScaleType.CENTER
                                )
                                lifecycleScope.launch {
                                    whenStarted {
                                        imageFile = reduceFileImage(imageFile!!)
                                    }
                                    dismissLoading()
                                }
                            } catch (e: Exception) {
                                dismissLoading()
                                showError(message = e.message, onClick = {
                                    imageFile = null
                                    binding.ivAddImage.apply {
                                        setImageResource(R.drawable.default_add_image)
                                        scaleType = null
                                    }
                                    dismissNotification()
                                })
                            }
                        }, WAIT_FOR_2000)
                    }
                }
                MENU_GALLERY -> {
                    val selectedImage: Uri = it.data?.data as Uri
                    showLoading(getString(R.string.message_loading_convert_image))
                    Handler(Looper.getMainLooper()).postDelayed({
                        imageFile = uriToFile(selectedImage, this)
                        try {
                            doGlide(
                                this,
                                binding.ivAddImage,
                                selectedImage,
                                scaleType = ImageView.ScaleType.CENTER
                            )
                            lifecycleScope.launch {
                                whenStarted {
                                    imageFile = reduceFileImage(imageFile!!)
                                }
                                dismissLoading()
                            }
                        } catch (e: Exception) {
                            dismissLoading()
                            showError(message = e.message, onClick = {
                                imageFile = null
                                binding.ivAddImage.apply {
                                    setImageResource(R.drawable.default_add_image)
                                    scaleType = null
                                }
                                dismissNotification()
                            })
                        }
                    }, WAIT_FOR_2000)
                }
                else -> {}
            }
        }
    }

    private fun addStory(story: Story?) {
        if (networkConnected()) {
            userLogin?.let { viewModel.addStory(it, story!!).observe(this, addStory) }
        }
    }

    private val addStory = Observer<DataResource<GeneralResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(getString(R.string.message_loading_add_story))
            }
            SUCCESS -> {
                dismissLoading()
                when (isResponseSuccess(GeneralResponse(it.data?.error, it.data?.message))) {
                    true -> successAddImage()
                    false -> {}
                }
            }
            ERROR -> {
                dismissLoading()
                wasError(it.generalResponse)
            }
            else -> {}
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Intent().apply {
                    putExtra(WAS_SUCCESS_ADD_STORY, wasSuccessAddStory)
                    setResult(RESULT_OK, this)
                }
                finish()
            }
        }

    private val onClickListener: OnClickListener = OnClickListener {
        when (it.id) {
            R.id.iv_add_image -> {
                showBottomOption(
                    getString(R.string.get_image_from),
                    imageMenus(),
                    onClickMenu = { menu ->
                        menuCode = menu.menuCode!!
                        when {
                            allPermissionsGranted() -> {
                                when (menu.menuCode) {
                                    MENU_CAMERA -> {
                                        resultLauncher.launch(
                                            Intent(
                                                this,
                                                CameraActivity::class.java
                                            )
                                        )
                                    }
                                    MENU_GALLERY -> {
                                        resultLauncher.launch(
                                            Intent.createChooser(
                                                Intent().apply {
                                                    action = ACTION_GET_CONTENT
                                                    type = IMAGE_FORMAT_GALLERY
                                                },
                                                getString(R.string.title_choose_image_from_gallery)
                                            )
                                        )
                                    }
                                    else -> {}
                                }
                            }
                            shouldShowRequestPermissionRationale(APP_PERMISSION_GET_IMAGE.toString()) -> {
                                showInformation(
                                    message = getString(R.string.message_failed_get_permission),
                                    onClick = {
                                        dismissNotification()
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                )
                            }
                            else -> {
                                requestPermissions(
                                    APP_PERMISSION_GET_IMAGE, ZERO
                                )
                            }
                        }
                        dismissBottomOption()
                    })
            }
            R.id.btn_add -> {
                validateAddStory()
            }
            R.id.btn_cancel -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun validateAddStory() {
        if (imageFile != null) {
            if (binding.edAddDescription.text.isNotEmpty()) {
                addStory(
                    Story(
                        description = binding.edAddDescription.text.toString().trim(),
                        photo = imageFile
                    )
                )
            } else ColorToast.roundColorWarning(
                this,
                getString(R.string.message_description_story_not_yet_add),
                Toast.LENGTH_LONG
            )
        } else ColorToast.roundColorWarning(
            this,
            getString(R.string.message_image_file_not_yet_add),
            Toast.LENGTH_LONG
        )
    }

    private fun allPermissionsGranted() = APP_PERMISSION_GET_IMAGE.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun successAddImage() {
        showSuccess(
            message = getString(R.string.message_success_add_story),
            onClick = {
                dismissNotification()
                wasSuccessAddStory = true
                onBackPressedDispatcher.onBackPressed()
            }
        )
    }
}