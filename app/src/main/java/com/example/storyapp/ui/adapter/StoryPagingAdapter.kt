package com.example.storyapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.story.Story
import com.example.storyapp.databinding.ItemStoryBinding

class StoryPagingAdapter(
    private val context: Context,
    private val listener: MainAdapter.StoryListener
) : PagingDataAdapter<Story, StoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.binding.apply {
            tvName.text = story?.name
            tvDate.text = story?.createdAt
            tvDescription.text = story?.description

            Glide.with(context)
                .load(story?.photoUrl)
                .into(imgStory)
        }
        holder.itemView.setOnClickListener {
            story?.let { listener.onClick(it) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}