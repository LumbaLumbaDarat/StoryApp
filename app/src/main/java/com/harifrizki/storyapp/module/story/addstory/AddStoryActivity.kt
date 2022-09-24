package com.harifrizki.storyapp.module.story.addstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harifrizki.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}