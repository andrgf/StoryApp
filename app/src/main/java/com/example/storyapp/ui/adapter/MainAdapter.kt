package com.example.storyapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.story.Story
import com.example.storyapp.databinding.ItemStoryBinding

class MainAdapter(
    private val listStory: List<Story>,
    val context: Context,
    private val listener: StoryListener
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    interface StoryListener {
        fun onClick(story: Story)
    }

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.binding.apply {
            tvName.text = story.name
            Glide.with(context)
                .load(story.photoUrl)
                .into(imgStory)
        }
        holder.itemView.setOnClickListener {
            listener.onClick(story)
        }
    }
}