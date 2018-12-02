package com.moviedb.nhdphuong.moviedb.ui.autocleared

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.FragmentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentActivityAutoClearedValue<T : Any>(activity: FragmentActivity, _value: T?) :
    ReadWriteProperty<FragmentActivity, T> {
    private var mValue: T? = null
    val value: T?
        get() = mValue

    init {
        mValue = _value
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mValue = null
            }
        })
    }

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        return mValue ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) {
        mValue = value
    }
}