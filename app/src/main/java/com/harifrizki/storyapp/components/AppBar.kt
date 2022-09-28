package com.harifrizki.storyapp.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.databinding.ComponentsAppBarBinding
import com.harifrizki.storyapp.utils.ZERO

class AppBar() {
    private var context: Context? = null
    private var binding: ComponentsAppBarBinding? = null
    var onClickIconAppBar: ((View) -> Unit)? = null

    companion object {
        const val USE_ONLY_TITLE = 1
        const val USE_WITH_ICON_WITH_CLICK = 2
    }

    fun create(context: Context?, binding: ComponentsAppBarBinding?) {
        this.context = context
        this.binding = binding
    }

    fun title(title: String?) {
        binding?.tvTitleApp?.apply {
            text = title
        }
    }

    fun subTitle(subTitle: String?) {
        binding?.tvSubTitleApp?.apply {
            text = subTitle
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBar(icon: Int? = ZERO, useMode: Int?) {
        when (useMode) {
            USE_ONLY_TITLE -> {
                binding?.ivBtnSetting?.apply {
                    visibility = View.INVISIBLE
                }
            }
            USE_WITH_ICON_WITH_CLICK -> {
                binding?.apply {
                    (if (icon != ZERO) icon else R.drawable.ic_round_settings_24)?.let {
                        ivBtnSetting.apply {
                            setImageResource(
                                it
                            )
                            background = context?.resources?.getDrawable(
                                R.drawable.button_transparent_ripple_primary,
                                null
                            )
                            context?.getColor(R.color.primary)
                                ?.let { color -> setColorFilter(color, PorterDuff.Mode.SRC_IN) }
                            visibility = View.VISIBLE
                            setOnClickListener {
                                onClickIconAppBar?.invoke(this)
                            }
                        }
                    }
                }
            }
        }
    }
}