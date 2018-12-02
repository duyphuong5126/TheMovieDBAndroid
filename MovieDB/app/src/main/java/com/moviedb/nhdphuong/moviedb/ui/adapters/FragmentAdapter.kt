package com.moviedb.nhdphuong.moviedb.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentAdapter(private val mFragmentList: List<Fragment>, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment = mFragmentList[p0]

    override fun getCount(): Int = mFragmentList.size
}