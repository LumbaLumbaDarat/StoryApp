package com.harifrizki.storyapp.module.story.liststory

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.components.AppBar.Companion.USE_WITH_ICON_WITH_CLICK
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.databinding.ActivityListStoryBinding
import com.harifrizki.storyapp.module.adapter.StoryAdapter
import com.harifrizki.storyapp.module.authentication.login.LoginActivity
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.module.story.addstory.AddStoryActivity
import com.harifrizki.storyapp.module.story.detailstory.DetailStoryActivity
import com.harifrizki.storyapp.utils.*
import com.harifrizki.storyapp.utils.MenuCode.MENU_ADD_STORY
import com.harifrizki.storyapp.utils.MenuCode.MENU_SETTING_LANGUAGE
import com.harifrizki.storyapp.utils.ResponseStatus.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListStoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val binding by lazy {
        ActivityListStoryBinding.inflate(layoutInflater)
    }
    private val userLogin: LoginResultResponse? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(MODEL_LOGIN, LoginResultResponse::class.java)
    }

    private val viewModel by viewModel<ListStoryViewModel>()
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
                        PreferencesManager.getInstance(this@ListStoryActivity)
                            .removePreferences(MODEL_LOGIN)
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
        if (it.resultCode == RESULT_OK) {
            if (it.data?.getBooleanExtra(WAS_SUCCESS_ADD_STORY, false) == true) story()
        }
    }

    override fun onRefresh() {
        binding.srlListStory.isRefreshing = false
        story()
    }

    private fun story() {
        if (networkConnected()) {
            userLogin?.let { viewModel.story(it).observe(this, this.story) }
        }
    }

    private val story = Observer<DataResource<GetAllStoriesResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                disableAccess()
                loadingList(true)
            }
            SUCCESS -> {
                when (isResponseSuccess(GeneralResponse(it.data?.error, it.data?.message))) {
                    true -> {
                        if (it.data?.stories?.size!! > ZERO) setStoryAdapter(it.data)
                        else {
                            showEmpty(
                                getString(R.string.app_name),
                                getString(R.string.message_story_not_found)
                            )
                            loadingList(isOn = false, isGetData = false)
                        }
                    }
                    false -> {
                        showEmpty(
                            getString(R.string.message_story_not_found),
                            it.data?.message
                        )
                        loadingList(isOn = false, isGetData = false)
                    }
                }
                enableAccess()
            }
            ERROR -> {
                loadingList(isOn = false, isGetData = false)
                showEmptyError(
                    getString(R.string.message_error_something_wrong),
                    it.generalResponse?.message
                )
                wasError(it.generalResponse)
                enableAccess()
            }
            else -> {}
        }
    }

    private fun initializeStory() {
        storyAdapter = StoryAdapter(context = this).apply {
            onClickStory = {
                resultLauncher.launch(
                    Intent(this@ListStoryActivity, DetailStoryActivity::class.java).apply {
                        putExtra(MODEL_STORY, it)
                    }
                )
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

    private fun setStoryAdapter(getAllStoriesResponse: GetAllStoriesResponse?) {
        storyAdapter?.apply {
            setStories(getAllStoriesResponse?.stories)
            notifyDataSetChanged()
            loadingList(isOn = false, isGetData = true)
        }
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