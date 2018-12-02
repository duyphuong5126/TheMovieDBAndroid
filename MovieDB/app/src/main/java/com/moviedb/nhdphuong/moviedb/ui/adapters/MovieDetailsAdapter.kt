package com.moviedb.nhdphuong.moviedb.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.moviedb.nhdphuong.moviedb.databinding.ItemMovieDetailBinding

class MovieDetailsAdapter(private val mDetailsList: List<Pair<String, String>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieDetailBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return KeyValueViewHolder(binding)
    }

    override fun getItemCount(): Int = mDetailsList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val keyValueViewHolder = viewHolder as KeyValueViewHolder
        keyValueViewHolder.setData(mDetailsList[position])
    }

    override fun getItemViewType(position: Int): Int = DetailType.KEY_VALUE.type

    private inner class KeyValueViewHolder(itemMovieBinding: ItemMovieDetailBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        private val tvInfoTitle = itemMovieBinding.tvInfoTitle
        private val tvInfo = itemMovieBinding.tvInfo

        fun setData(keyValue: Pair<String, String>) {
            tvInfoTitle.text = keyValue.first
            tvInfo.text = keyValue.second
        }
    }

    enum class DetailType(val type: Int) {
        KEY_VALUE(10056)
    }
}