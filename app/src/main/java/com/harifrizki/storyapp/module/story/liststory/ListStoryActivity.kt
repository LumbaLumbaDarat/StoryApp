package com.harifrizki.storyapp.module.story.liststory

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.components.AppBar.Companion.USE_WITH_ICON_WITH_CLICK
import com.harifrizki.storyapp.databinding.ActivityListStoryBinding
import com.harifrizki.storyapp.module.adapter.StoryAdapter
import com.harifrizki.storyapp.module.base.BaseActivity

class ListStoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val binding by lazy {
        ActivityListStoryBinding.inflate(layoutInflater)
    }

    private var storyAdapter: StoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
            initializeStory()
            createAppBar(
                appBar,
                title = getString(R.string.app_name),
                subTitle = getString(R.string.app_sub),
                useMode = USE_WITH_ICON_WITH_CLICK,
                onClick = {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                })
            createEmpty(emptyData)
            srlListStory.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(this@ListStoryActivity, this)
                setOnRefreshListener(this@ListStoryActivity)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    override fun onRefresh() {
        binding.srlListStory.isRefreshing = false
    }

    private fun story() {
        if (networkConnected()) {

        }
    }

    private fun initializeStory() {
        storyAdapter = StoryAdapter(context = this).apply {
            onClickStory = {

            }
        }
        binding.apply {
            rvListOfStory.apply {
                layoutManager = LinearLayoutManager(this@ListStoryActivity)
                adapter = storyAdapter
            }
            rvListOfStoryShimmer.apply {
                layoutManager = LinearLayoutManager(this@ListStoryActivity)
                adapter = StoryAdapter(
                    context = this@ListStoryActivity,
                    isShimmer = true
                )
            }
        }
    }
}