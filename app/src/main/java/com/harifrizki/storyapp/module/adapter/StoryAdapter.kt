package com.harifrizki.storyapp.module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harifrizki.storyapp.databinding.ItemStoryBinding
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.utils.doGlide
import com.harifrizki.storyapp.utils.getMaxShimmerList
import com.harifrizki.storyapp.utils.layoutStartDrawableShimmer
import com.harifrizki.storyapp.utils.widgetStartDrawableShimmer

class StoryAdapter(
    var context: Context?,
    var isShimmer: Boolean? = false
) : RecyclerView.Adapter<StoryAdapter.HolderView>() {
    var stories: ArrayList<Story>? = ArrayList()
    var onClickStory: ((Story?) -> Unit)? = null

    @JvmName("initializeAdmins")
    @SuppressLint("NotifyDataSetChanged")
    fun setStories(stories: ArrayList<Story>?) {
        this.stories?.apply {
            clear()
            addAll(stories!!)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAdmins(stories: ArrayList<Story>?) {
        this.stories?.apply {
            addAll(stories!!)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        return HolderView(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val story: Story
        if (!isShimmer!!) {
            story = stories!![position]
            holder.apply {
                itemView.setOnClickListener {
                    onClickStory?.invoke(story)
                }
            }
        } else story = Story()
        holder.bind(story, isShimmer)
    }

    override fun getItemCount(): Int {
        return if (isShimmer!!) getMaxShimmerList()
        else stories!!.size
    }

    class HolderView(
        val binding: ItemStoryBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            story: Story?,
            isShimmer: Boolean?
        ) {
            binding.apply {
                if (!isShimmer!!) {
                    doGlide(
                        context,
                        ivItemPhoto,
                        story?.photoUrl
                    )
                    tvItemName.text = story?.name
                    tvItemDescription.text = story?.description
                    tvItemDate.text = story?.createdAt
                } else {
                    layoutStartDrawableShimmer(
                        arrayOf(
                            llBackground
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            ivItemPhoto
                        ), context
                    )
                    widgetStartDrawableShimmer(
                        arrayOf(
                            tvItemName, tvItemDescription, tvItemDate
                        ), context
                    )
                }
            }
        }
    }
}