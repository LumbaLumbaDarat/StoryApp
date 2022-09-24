package com.harifrizki.storyapp.components

import android.view.View
import com.harifrizki.storyapp.databinding.ComponentsEmptyDataBinding

class EmptyData() {
    private var binding: ComponentsEmptyDataBinding? = null

    fun create(binding: ComponentsEmptyDataBinding?) {
        this.binding = binding
    }

    fun animation(animationEmpty: String?,
                  useAnimation: Boolean? = true) {
        binding?.ltEmpty?.apply {
            setAnimation(animationEmpty)
            if (useAnimation!!)
                playAnimation()
            else pauseAnimation()
        }
    }

    fun message(title: String?, message: String?) {
        binding?.apply {
            tvTitleMessageEmpty.text = title
            tvMessageEmpty.text = message
        }
    }

    fun show() {
        binding?.root?.visibility = View.VISIBLE
    }

    fun dismiss() {
        binding?.root?.visibility = View.GONE
    }
}