package com.harifrizki.storyapp.module.story.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityDetailStoryBinding
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.widgetStartDrawableShimmer

class DetailStoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val binding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

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
            createRootView(detailStory, sflDetailStory)
            createEmpty(emptyData)
            initializeDetailStory()
            srlDetailStory.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(this@DetailStoryActivity, this)
                setOnRefreshListener(this@DetailStoryActivity)
            }
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        detailStory()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    override fun onRefresh() {
        binding.srlDetailStory.isRefreshing = false
        detailStory()
    }

    private fun detailStory() {
        if (networkConnected()) {
            disableAccess()
            loadingList(true)
            Handler().postDelayed({
                loadingList(false, false)
                enableAccess()
            }, 1000)
        }
    }

    private fun initializeDetailStory() {
        binding.detailStory.apply {
            widgetStartDrawableShimmer(
                arrayOf(
                    ivDetailPhoto
                ), this@DetailStoryActivity
            )
            widgetStartDrawableShimmer(
                arrayOf(
                    tvDetailName, tvDetailDescription
                ), this@DetailStoryActivity
            )
        }
    }

    private fun enableAccess() {
        enableAccess(arrayOf(binding.btnBack))
    }

    private fun disableAccess() {
        disableAccess(arrayOf(binding.btnBack))
    }
}