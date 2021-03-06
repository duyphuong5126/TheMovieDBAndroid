package com.moviedb.nhdphuong.moviedb.ui.home

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.moviedb.nhdphuong.moviedb.R
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.databinding.FragmentFavoriteListBinding
import com.moviedb.nhdphuong.moviedb.ui.adapters.MoviesAdapter
import com.moviedb.nhdphuong.moviedb.ui.autocleared.AutoClearedValue
import com.moviedb.nhdphuong.moviedb.ui.details.MovieDetailsActivity
import com.moviedb.nhdphuong.moviedb.viewmodels.AppViewModelFactory
import com.moviedb.nhdphuong.moviedb.viewmodels.FavoriteViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteListFragment : Fragment() {
    companion object {
        private const val TAG = "NowShowingFragment"
        private const val GRID_COLUMNS = 3
        private const val LANDSCAPE_GRID_COLUMNS = 4
    }

    private var mBinding: AutoClearedValue<FragmentFavoriteListBinding>? = null

    @Inject
    lateinit var mViewModelFactory: AppViewModelFactory

    private lateinit var mFavoriteViewModel: FavoriteViewModel

    private var mMoviesAdapter: MoviesAdapter? = null

    private var isLoading = false

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        )
        mBinding = AutoClearedValue(this, binding)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFavoriteViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FavoriteViewModel::class.java)
        mMoviesAdapter = MoviesAdapter(mFavoriteViewModel.moviesList)

        mMoviesAdapter?.setCallback(object : MoviesAdapter.ItemClickCallback {
            override fun onItemClick(movie: Movie) {
                Log.d(TAG, "Movie ${movie.id} is chosen")
                context?.run {
                    MovieDetailsActivity.start(this, movie)
                }
            }
        })
        val isLandscape = resources.getBoolean(R.bool.is_landscape)
        val spanCount = if (isLandscape) LANDSCAPE_GRID_COLUMNS else GRID_COLUMNS
        val gridLayoutManager = object : GridLayoutManager(context, spanCount) {
            override fun isAutoMeasureEnabled(): Boolean = true
        }
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mMoviesAdapter?.getItemViewType(position)) {
                    MoviesAdapter.TYPE_LOADING -> spanCount
                    else -> 1
                }
            }
        }

        mBinding?.value?.rvNowShowing?.run {
            adapter = mMoviesAdapter
            layoutManager = gridLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = gridLayoutManager.childCount
                    val totalItemCount = gridLayoutManager.itemCount
                    val pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + pastVisibleItems >= totalItemCount && mMoviesAdapter?.isLoading == false) {
                        mMoviesAdapter?.addLoadingItem()
                        mFavoriteViewModel.downloadNextPage()
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mFavoriteViewModel.registerRemoteMoviesListChangedListener(this, this::onMoviesListChanged)
        mFavoriteViewModel.registerNetworkErrorListener(this, this::onNetworkFailed)
        if (!mFavoriteViewModel.isInitialLoaded) {
            showLoading()
            mFavoriteViewModel.downloadInitialMovesList()
        }
    }

    override fun onStop() {
        super.onStop()
        mFavoriteViewModel.unRegisterRemoteMoviesListChangedListener(this)
        mFavoriteViewModel.unRegisterNetworkErrorListener(this)
    }

    /**
     * Detect tab changed
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "$TAG is shown")
        if (this::mFavoriteViewModel.isInitialized) {
            mFavoriteViewModel.downloadNextPage()
        }
    }

    private fun onMoviesListChanged(newList: List<Movie>, pageNumber: Int) {
        Log.d(TAG, "Page $pageNumber, movies: ${newList.size}")
        if (pageNumber == 1) {
            hideLoading()
        }
        mMoviesAdapter?.run {
            if (itemCount > 0) {
                removeLoadingItem()
            }
            if (newList.isNotEmpty()) {
                addItems(newList)
            }
        }
    }

    private fun onNetworkFailed(message: String) {
        Toast.makeText(context, "Network error: $message", Toast.LENGTH_SHORT).show()
        hideLoading()
        mMoviesAdapter?.removeLoadingItem()
    }

    private fun showLoading() {
        if (!isLoading) {
            isLoading = true
            mBinding?.value?.run {
                pbLoading.visibility = View.VISIBLE
                rvNowShowing.visibility = View.GONE
            }
        }
    }

    private fun hideLoading() {
        if (isLoading) {
            isLoading = false
            mBinding?.value?.run {
                pbLoading.visibility = View.GONE
                rvNowShowing.visibility = View.VISIBLE
            }
        }
    }
}