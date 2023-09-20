package com.example.storyapp.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.remote.story.Story
import com.example.storyapp.networks.ApiService
import com.example.storyapp.util.LoginPref

class StoryPagingSource(
    private val apiService: ApiService,
    private val context: Context
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: 1
            val pageSize = params.loadSize
            val token = LoginPref(context).getToken()
            val response = apiService.getStory("Bearer $token", position, pageSize)

            val stories = response.body()?.listStory ?: emptyList()

            val prevKey = if (position == 1) null else position -1
            val nextKey = if (stories.isEmpty()) null else position +1
            LoadResult.Page(
                data = stories,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}