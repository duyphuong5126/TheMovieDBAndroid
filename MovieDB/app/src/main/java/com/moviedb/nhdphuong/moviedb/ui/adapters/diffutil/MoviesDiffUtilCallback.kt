package com.moviedb.nhdphuong.moviedb.ui.adapters.diffutil

import android.support.v7.util.DiffUtil
import com.moviedb.nhdphuong.moviedb.data.entities.Movie

class MoviesDiffUtilCallback(private val oldList: List<Movie>, private val newList: List<Movie>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}