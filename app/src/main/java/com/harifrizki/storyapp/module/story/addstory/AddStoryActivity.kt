package com.harifrizki.storyapp.module.story.addstory

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityAddStoryBinding
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.makeSpannable

class AddStoryActivity : BaseActivity() {
    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }

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

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

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
                    onClickMenu = {
                        dismissBottomOption()
                    })
            }
            R.id.btn_add -> {

            }
            R.id.btn_cancel -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}