package com.harifrizki.storyapp.module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.storyapp.databinding.ItemMenuBinding
import com.harifrizki.storyapp.model.Menu
import com.harifrizki.storyapp.utils.checkBuildOS

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.HolderView>() {
    var menus: ArrayList<Menu>? = ArrayList()
    var onClickMenu: ((Menu) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addMenus(menus: ArrayList<Menu>?) {
        this.menus?.apply {
            clear()
            addAll(menus!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val menu = menus!![position]
        holder.apply {
            bind(menu)
            itemView.setOnClickListener {
                onClickMenu?.invoke(menu)
            }
        }
    }

    override fun getItemCount(): Int {
        return menus!!.size
    }

    inner class HolderView(
        private val binding: ItemMenuBinding,
        private val context: Context?):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu?) = with(binding)
        {
            if (menu?.visibility!!)
            {
                tvNameMenu.apply {
                    text = menu.name
                    if (checkBuildOS(Build.VERSION_CODES.M))
                        menu.nameColor?.let {
                            setTextColor(ContextCompat.getColor(context!!, it)) }
                    else menu.nameColor?.let { setTextColor(context!!.getColor(it)) }
                }
                ivIconMenu.apply {
                    menu.useIcon?.let { setImageResource(it) }
                    menu.iconColor?.let {
                        ContextCompat.getColor(
                            context!!,
                            it
                        )
                    }?.let {
                        setColorFilter(
                            it, android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
                }
            }
        }
    }
}