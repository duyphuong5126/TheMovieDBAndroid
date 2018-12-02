package com.moviedb.nhdphuong.moviedb.ui.home

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.moviedb.nhdphuong.moviedb.R
import com.moviedb.nhdphuong.moviedb.databinding.ActivityMainBinding
import com.moviedb.nhdphuong.moviedb.ui.adapters.FragmentAdapter
import com.moviedb.nhdphuong.moviedb.ui.autocleared.FragmentActivityAutoClearedValue


class HomeActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "HomeActivity"
    }

    private var mHomeActivityBinding: FragmentActivityAutoClearedValue<ActivityMainBinding>? = null

    private var mFragmentList: ArrayList<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.inflate<ActivityMainBinding>(layoutInflater, R.layout.activity_main, null, false)
        mHomeActivityBinding = FragmentActivityAutoClearedValue(this, binding)
        setContentView(binding?.root)

        mFragmentList = ArrayList()
        mFragmentList?.add(NowShowingFragment())
        mFragmentList?.add(FavoriteListFragment())
        mHomeActivityBinding?.value?.run {
            vpHomeTabs.adapter = FragmentAdapter(mFragmentList!!, supportFragmentManager)

            tvNowShowing.setOnClickListener {
                Log.d(TAG, "Now showing tab is chosen")
                vpHomeTabs.setCurrentItem(0, true)
                toggletabIndicators(Tab.NOW_SHOWING)
            }

            tvFavorite.setOnClickListener {
                Log.d(TAG, "Favorite tab is chosen")
                vpHomeTabs.setCurrentItem(1, true)
                toggletabIndicators(Tab.FAVORITE)
            }
        }
    }

    private fun toggletabIndicators(tab: Tab) {
        mHomeActivityBinding?.value?.run {
            when (tab) {
                Tab.NOW_SHOWING -> {
                    tvNowShowing.setBackgroundResource(R.drawable.bg_white_bottom_green)
                    tvFavorite.setBackgroundResource(R.color.white)
                }
                else -> {
                    tvNowShowing.setBackgroundResource(R.color.white)
                    tvFavorite.setBackgroundResource(R.drawable.bg_white_bottom_green)
                }
            }
        }
    }

    private enum class Tab {
        NOW_SHOWING,
        FAVORITE
    }
}
