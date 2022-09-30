package com.harifrizki.storyapp.module.story.addstory

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityAddStoryBinding
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.camerapage.CameraActivity
import com.harifrizki.storyapp.utils.*
import com.harifrizki.storyapp.utils.MenuCode.*
import com.lumbalumbadrt.colortoast.ColorToast
import java.io.File

class AddStoryActivity : BaseActivity() {
    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }

    private var menuCode: MenuCode = MENU_NONE
    private var imageFile: File? = null

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
                    if (it.data?.getBooleanExtra(WAS_SUCCESS_GET_IMAGE, false) == true)
                    {
                        @Suppress("DEPRECATION")
                        imageFile = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                            it.data?.getSerializableExtra(RESULT_CAPTURE_IMAGE, File::class.java)
                        else it.data?.getSerializableExtra(RESULT_CAPTURE_IMAGE) as File
                        val isBackFromCamera = it.data?.getBooleanExtra(IS_BACK_FROM_CAMERA, true)
                        val result = isBackFromCamera?.let { back ->
                            rotateBitmap(
                                BitmapFactory.decodeFile(imageFile?.path),
                                back
                            )
                        }
                        binding.ivAddImage.setImageBitmap(result)
                    }
                }
                MENU_GALLERY -> {
                    val selectedImage: Uri = it.data?.data as Uri
                    val imageFile = uriToFile(selectedImage, this)
                    binding.ivAddImage.setImageURI(selectedImage)
                }
                else -> {}
            }
        }
    }

    private fun addStory() {
        if (networkConnected()) {
            showLoading(
                message = getString(R.string.message_loading_add_story)
            )
            Handler().postDelayed({
                dismissLoading()
            }, 1000)
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
            if (binding.edAddDescription.text.isNotEmpty())
            {
                addStory()
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
}