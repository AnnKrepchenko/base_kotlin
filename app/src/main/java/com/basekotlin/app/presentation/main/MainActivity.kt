package com.basekotlin.app.presentation.main

import android.content.Intent
import android.net.Uri
import android.support.v4.view.GravityCompat
import android.view.View
import com.basekotlin.app.R
import com.basekotlin.app.core.BaseApp
import com.basekotlin.app.presentation.base.BaseActivity
import com.basekotlin.app.presentation.main.webview.URLChangedInterface
import com.basekotlin.app.presentation.main.webview.WebViewFragment
import com.basekotlin.app.utils.DoubleClickPreventer
import com.basekotlin.app.utils.UiUtils
import com.basekotlin.app.utils.animation.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity<MainPresenter<MainView>, MainView>(), MainView, View.OnClickListener, URLChangedInterface {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFonts() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var headerNavigationView: View
    private lateinit var webViewFragment: WebViewFragment
    private var animate = 0
    private val bottomResList = listOf(R.id.action_feed, R.id.action_library, R.id.action_chat, R.id.action_analytics, R.id.action_people)

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun providePresenter(): MainPresenter<MainView> {
        return MainPresenterImpl(BaseApp.provideApp(this))
    }

    override fun provideView(): MainView {
        return this
    }

    override fun initViews() {
        webViewFragment = WebViewFragment()
        webViewFragment.urlJsInterface = this
        nav_view.getChildAt(0)?.isVerticalScrollBarEnabled = false
        headerNavigationView = nav_view.getHeaderView(0)
        btmNavView.setOnNavigationItemSelectedListener(
                { item ->
                    DoubleClickPreventer.onClickWithCustomInterval(50L, object : DoubleClickPreventer.OnNextListener {
                        override fun onNext() {
                            webViewFragment.position = bottomResList.indexOf(item.itemId)
                            item.isChecked = true
                        }
                    }, DoubleClickPreventer.MIN_CLICK_INTERVAL_100_MS)
                    false
                })
        btmNavView.selectedItemId = bottomResList[0]
        addWebView()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun addWebView() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.flContainer, webViewFragment, "webview")
                .disallowAddToBackStack()
                .commit()
    }

    override fun onUrlChange(url: String) {
    }

    override fun handleOtherUrl(url: String?) {
        url ?: return
        startWebView(url)
    }

    override fun hideBottomBar() {
        BottomNavigationViewHelper.setHidden(btmNavView, true)
        flContainer.layoutParams = UiUtils.getCoordinatorParams(0)
    }

    override fun showBottomBar() {
        BottomNavigationViewHelper.setHidden(btmNavView, false)
        flContainer.layoutParams = UiUtils.getCoordinatorParams(UiUtils.getActionBarSize(this))
    }

    private fun startWebView(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}