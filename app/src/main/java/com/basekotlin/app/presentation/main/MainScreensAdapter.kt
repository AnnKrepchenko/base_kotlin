package com.basekotlin.app.presentation.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.basekotlin.app.presentation.main.webview.WebViewFragment


class MainScreensAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val FEED_SCREEN_POSITION = 0
    val LIBRARY_SCREEN_POSITION = 1
    val ANALYTICS_SCREEN_POSITION = 2
    val CHAT_SCREEN_POSITION = 3
    val PEOPLE_SCREEN_POSITION = 4

    var uuid: String? = null
        set(value) {
            reloadAll(value)
        }
    private var registeredFragments = SparseArray<WebViewFragment>()


    override fun getItem(position: Int): Fragment {
        val fragment = WebViewFragment()
        fragment.position = position
        return fragment
    }

    override fun getCount(): Int {
        return 5
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as WebViewFragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    private fun reloadAll(value: String?) {
        (0..registeredFragments.size())
                .map { registeredFragments[it] }
                .forEach { it?.uuid = value }
    }
}
