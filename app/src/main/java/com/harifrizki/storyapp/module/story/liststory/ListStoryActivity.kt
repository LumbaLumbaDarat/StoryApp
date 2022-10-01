package com.harifrizki.storyapp.module.story.liststory

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.components.AppBar.Companion.USE_WITH_ICON_WITH_CLICK
import com.harifrizki.storyapp.databinding.ActivityListStoryBinding
import com.harifrizki.storyapp.module.adapter.StoryAdapter
import com.harifrizki.storyapp.module.authentication.login.LoginActivity
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.story.addstory.AddStoryActivity
import com.harifrizki.storyapp.module.story.detailstory.DetailStoryActivity
import com.harifrizki.storyapp.utils.MODEL_LOGIN
import com.harifrizki.storyapp.utils.MenuCode.*
import com.harifrizki.storyapp.utils.PreferencesManager
import com.harifrizki.storyapp.utils.ZERO

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
                    initializeMainMenu(it)
                })
            createRootView(rvListOfStory, sflListOfStory)
            createEmpty(emptyData)
            srlListStory.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(this@ListStoryActivity, this)
                setOnRefreshListener(this@ListStoryActivity)
            }
            actionLogout.setOnClickListener {
                showOption(
                    getString(R.string.title_logout),
                    getString(R.string.message_logout),
                    onPositive = {
                        dismissOption()
                        PreferencesManager.getInstance(this@ListStoryActivity).removePreferences(MODEL_LOGIN)
                        startActivity(
                            Intent(
                                this@ListStoryActivity,
                                LoginActivity::class.java
                            )
                        )
                        finish()
                    },
                    onNegative = { dismissOption() }
                )
            }
        }
        story()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    override fun onRefresh() {
        binding.srlListStory.isRefreshing = false
        story()
    }

    private fun story() {
        if (networkConnected()) {
            disableAccess()
            loadingList(true)
            Handler().postDelayed({
                loadingList(false, false)
                enableAccess()
            }, 1000)
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

    private fun initializeMainMenu(view: View?) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        showOptionList(view,
            mainMenus(),
            R.drawable.frame_background_white,
            view?.width?.minus(325),
            20,
            onClickMenu = {
                when (it.menuCode) {
                    MENU_ADD_STORY -> {
                        dismissOptionList()
                        resultLauncher.launch(Intent(this, AddStoryActivity::class.java))
                    }
                    MENU_SETTING_LANGUAGE -> {
                        dismissOptionList()
                        resultLauncher.launch(Intent(this, DetailStoryActivity::class.java))
                        //resultLauncher.launch(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }
                    else -> {}
                }
            })
    }

    private fun enableAccess() {
        enableAccess(arrayOf(binding.actionLogout))
        enableAccess(arrayOf(binding.appBar.ivBtnSetting))
    }

    private fun disableAccess() {
        disableAccess(arrayOf(binding.actionLogout))
        disableAccess(arrayOf(binding.appBar.ivBtnSetting))
    }
}