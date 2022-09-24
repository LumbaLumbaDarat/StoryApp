package com.harifrizki.storyapp.module.story.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}