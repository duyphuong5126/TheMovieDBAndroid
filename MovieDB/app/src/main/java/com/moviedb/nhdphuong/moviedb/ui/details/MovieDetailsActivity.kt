package com.moviedb.nhdphuong.moviedb.ui.details

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.moviedb.nhdphuong.moviedb.R
import com.moviedb.nhdphuong.moviedb.data.entities.Movie
import com.moviedb.nhdphuong.moviedb.databinding.ActivityMovieDetailsBinding
import com.moviedb.nhdphuong.moviedb.support.ImageHelper
import com.moviedb.nhdphuong.moviedb.ui.adapters.MovieDetailsAdapter
import com.moviedb.nhdphuong.moviedb.ui.autocleared.FragmentActivityAutoClearedValue
import com.moviedb.nhdphuong.moviedb.viewmodels.AppViewModelFactory
import com.moviedb.nhdphuong.moviedb.viewmodels.MovieDetailsViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {
    companion object {
        private const val MOVIE_DATA = "MOVIE_DATA"

        private const val DEFAULT_ALPHA = 0.4f
        private const val FAVORITE_ALPHA = 1f

        fun start(context: Context, movie: Movie) {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(MOVIE_DATA, movie)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var mViewModelFactory: AppViewModelFactory

    private var mBinding: FragmentActivityAutoClearedValue<ActivityMovieDetailsBinding>? = null

    private var mMovieDetailsViewModel: MovieDetailsViewModel? = null

    private var mMovieDetailsAdapter: MovieDetailsAdapter? = null

    private var mFavoriteColorTint = 0
    private var mDefaultColorTint = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.inflate<ActivityMovieDetailsBinding>(
            layoutInflater,
            R.layout.activity_movie_details,
            null,
            false
        )
        setContentView(binding.root)
        mFavoriteColorTint = ContextCompat.getColor(this, R.color.red_1)
        mDefaultColorTint = ContextCompat.getColor(this, R.color.grey_4)

        mBinding = FragmentActivityAutoClearedValue(this, binding)
        val movie = intent?.extras?.getSerializable(MOVIE_DATA) as Movie

        mMovieDetailsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MovieDetailsViewModel::class.java)
        mMovieDetailsViewModel?.setUpData(movie, this::onDataReady)

        mBinding?.value?.run {
            ivBookmark.setOnClickListener {
                mMovieDetailsViewModel?.changeFavoriteStatus()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mMovieDetailsViewModel?.registerFavoriteStatusChanged(this, this::onFavoriteStatusChanged)
        mMovieDetailsViewModel?.checkFavoriteStatus()
    }

    override fun onStop() {
        super.onStop()
        mMovieDetailsViewModel?.unRegisterFavoriteStatusChanged(this)
    }

    private fun onDataReady(
        bigTitle: String, smallTitle: String, overview: String, backdropUrl: String, posterUrl: String,
        detailsList: List<Pair<String, String>>
    ) {
        mBinding?.value?.run {
            tvTitleBig.text = bigTitle
            tvTitleSmall.text = smallTitle
            tvOverview.text = overview

            val placeHolderImage = R.drawable.bg_image_not_found
            ImageHelper.loadImage(backdropUrl, placeHolderImage, ivBackdrop)
            ImageHelper.loadImage(posterUrl, placeHolderImage, ivPoster)

            mMovieDetailsAdapter = MovieDetailsAdapter(detailsList)
            val linearLayoutManager = object : LinearLayoutManager(this@MovieDetailsActivity) {
                override fun isAutoMeasureEnabled(): Boolean {
                    return true
                }
            }
            rvMovieDetails.run {
                adapter = mMovieDetailsAdapter
                layoutManager = linearLayoutManager
            }
        }
    }

    private fun onFavoriteStatusChanged(isFavorite: Boolean) {
        mBinding?.value?.run {
            ivBookmark.setColorFilter(if (isFavorite) mFavoriteColorTint else mDefaultColorTint)
            ivBookmark.alpha = if (isFavorite) FAVORITE_ALPHA else DEFAULT_ALPHA
            tvFavorite.visibility = if (isFavorite) View.VISIBLE else View.GONE
        }
    }
}
