package com.harifrizki.storyapp.components

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harifrizki.storyapp.databinding.ComponentsBottomOptionBinding
import com.harifrizki.storyapp.model.Menu
import com.harifrizki.storyapp.module.adapter.MenuAdapter

class BottomOption(
    var title: String? = null,
    var options: ArrayList<Menu>? = null
) : BottomSheetDialogFragment() {
    private var binding: ComponentsBottomOptionBinding? = null
    var onClick: ((Menu?) -> Unit)? = null

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        binding = ComponentsBottomOptionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding!!.root)

        binding?.apply {
            tvTitleBottomOption.text = title
            rvOption.apply {
                layoutManager =
                    LinearLayoutManager(context)
                adapter = MenuAdapter().apply {
                    this.menus = options
                    onClickMenu = {
                        onClick?.invoke(it)
                    }
                }
            }
            btnCancelBottomOption.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}