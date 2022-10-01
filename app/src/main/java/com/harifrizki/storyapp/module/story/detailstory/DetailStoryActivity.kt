package com.harifrizki.storyapp.module.story.detailstory

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ActivityDetailStoryBinding
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.module.base.BaseActivity
import com.harifrizki.storyapp.utils.MODEL_STORY
import com.harifrizki.storyapp.utils.RESULT_CAPTURE_IMAGE
import com.harifrizki.storyapp.utils.doGlide
import com.harifrizki.storyapp.utils.widgetStartDrawableShimmer
import java.io.File

@Suppress("DEPRECATION")
class DetailStoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val binding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    private var story: Story? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(MODEL_STORY, Story::class.java)
        else intent.getParcelableExtra(MODEL_STORY)

        binding.apply {
            initializeDetailStory()
            createAppBar(
                appBar,
                title = getString(R.string.app_name),
                subTitle = getString(R.string.app_sub)
            )
            createRootView(detailStory, sflDetailStory)
            createEmpty(emptyData)
            srlDetailStory.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(this@DetailStoryActivity, this)
                setOnRefreshListener(this@DetailStoryActivity)
            }
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        detailStory(story)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {

    }

    override fun onRefresh() {
        binding.srlDetailStory.isRefreshing = false
    }

    private fun detailStory(story: Story?) {
        binding.apply {
            doGlide(
                this@DetailStoryActivity,
                detailStory.ivDetailPhoto,
                story?.photoUrl,
                scaleType = ImageView.ScaleType.CENTER
            )
            detailStory.tvDetailName.text = story?.name
            detailStory.tvDetailDescription.text = story?.description
        }
    }

    private fun initializeDetailStory() {
        binding.detailStoryShimmer.apply {
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
}