package com.harifrizki.storyapp.components

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.storyapp.databinding.ComponentsOptionListBinding
import com.harifrizki.storyapp.model.Menu
import com.harifrizki.storyapp.module.adapter.MenuAdapter

class OptionList(
    var popupWindow: PopupWindow? = null
) {
    private var binding: ComponentsOptionListBinding? = null
    var onClick: ((Menu?) -> Unit)? = null

    fun create(context: Context?) {
        binding = ComponentsOptionListBinding.inflate(LayoutInflater.from(context))
        popupWindow = PopupWindow(
            binding?.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            animationStyle = android.R.style.Animation_Dialog
        }
    }

    fun setBackground(drawable: Drawable?) {
        binding?.background?.background = drawable
    }

    fun setMenus(options: ArrayList<Menu>?) {
        binding?.rvList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MenuAdapter().
            apply {
                menus = options
                onClickMenu = {
                    onClick?.invoke(it)
                }
            }
        }
    }

    fun show(view: View?, xOff: Int?, yOff: Int?) {
        popupWindow?.elevation = 20F
        popupWindow?.showAsDropDown(view, xOff!!, yOff!!)
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }
}