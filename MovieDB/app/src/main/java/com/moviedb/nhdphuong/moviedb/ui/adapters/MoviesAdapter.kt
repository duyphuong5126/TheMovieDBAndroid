package com.moviedb.nhdphuong.moviedb.ui.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.moviedb.nhdphuong.moviedb.R
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.databinding.ItemLoadingBinding
import com.moviedb.nhdphuong.moviedb.databinding.ItemMovieBinding
import com.moviedb.nhdphuong.moviedb.support.ImageHelper
import com.moviedb.nhdphuong.moviedb.ui.adapters.diffutil.MoviesDiffUtilCallback
import java.util.concurrent.atomic.AtomicBoolean

class MoviesAdapter(private val mMovies: MutableList<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TAG = "MoviesAdapter"
        const val TYPE_LOADING = -1
        const val TYPE_MOVIE = 1

        const val DUMMY_ID = -1L
    }

    private var itemClickCallback: ItemClickCallback? = null
    private var mIsLoading = AtomicBoolean(false)
    val isLoading
        get() = mIsLoading.get()

    private val mLoadingItem = Movie(
        DUMMY_ID, "", "", "", false, false,
        emptyList(), "", 0, 0f, 0.0, "", "", ""
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder(ItemLoadingBinding.inflate(layoutInflater, viewGroup, false))
            else -> MovieViewHolder(ItemMovieBinding.inflate(layoutInflater, viewGroup, false))
        }
    }

    override fun getItemCount(): Int = mMovies.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_MOVIE -> {
                val movieViewHolder = viewHolder as MovieViewHolder
                movieViewHolder.setData(mMovies[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mMovies[position].isDummy()) TYPE_LOADING else TYPE_MOVIE
    }

    fun addItems(moviesList: List<Movie>) {
        if (mMovies.containsAll(moviesList)) {
            Log.d(TAG, "Inputted list is already contained in adapter")
            return
        }
        val newResultList = ArrayList<Movie>()
        newResultList.addAll(mMovies)
        newResultList.addAll(moviesList)

        val diffResult = DiffUtil.calculateDiff(
            MoviesDiffUtilCallback(
                mMovies,
                newResultList
            )
        )
        mMovies.addAll(moviesList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addLoadingItem() {
        if (!isLoading) {
            mMovies.add(mLoadingItem)
            notifyItemInserted(mMovies.size - 1)
            mIsLoading.set(true)
        }
    }

    fun removeLoadingItem() {
        if (isLoading) {
            mMovies.removeAt(mMovies.size - 1)
            notifyItemRemoved(mMovies.size)
            mIsLoading.set(false)
        }
    }

    fun setCallback(itemClickCallback: ItemClickCallback) {
        this.itemClickCallback = itemClickCallback
    }

    private inner class MovieViewHolder(itemMovieBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        private var mData: Movie? = null
        private var mIvPoster: ImageView = itemMovieBinding.ivMoviePoster

        init {
            itemMovieBinding.ivMoviePoster.setOnClickListener {
                mData?.run {
                    itemClickCallback?.onItemClick(this)
                }
            }
        }

        fun setData(movie: Movie) {
            mData = movie
            ImageHelper.loadImage(movie.posterUrl, R.drawable.bg_image_not_found, mIvPoster)
        }
    }

    private inner class LoadingViewHolder(itemLoadingBinding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(itemLoadingBinding.root)

    interface ItemClickCallback {
        fun onItemClick(movie: Movie)
    }
}