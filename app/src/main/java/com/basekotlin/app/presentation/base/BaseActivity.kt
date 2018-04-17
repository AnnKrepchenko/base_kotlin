package com.basekotlin.app.presentation.base

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.annotation.StringRes
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.basekotlin.app.R
import com.basekotlin.app.core.BaseApp
import com.basekotlin.app.presentation.LoadingDialog
import com.basekotlin.app.presentation.NoInternetCallback
import com.basekotlin.app.presentation.Presenter
import com.basekotlin.app.presentation.View
import com.basekotlin.app.utils.NetworkChangeReceiver


abstract class BaseActivity<T : Presenter<V>, V : View> : BaseFragmentActivity(), View, NoInternetCallback {
    private val HANDLER_DELAY = 300
    protected var presenter: T? = null
    private var dialog: ProgressDialog? = null
    private var rootView: ViewGroup? = null
    private var loadingDialog: LoadingDialog? = null
    private var alertDialog: AlertDialog? = null


    protected abstract val layoutRes: Int

    protected val isAllowAction: Boolean
        get() {
            return if (!NetworkChangeReceiver.isNetworkConnected(this)) {
                Handler().postDelayed({ showNoInternetConnectionToast() }, HANDLER_DELAY.toLong())
                false
            } else {
                true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        initViews()
        presenter = providePresenter()
        presenter?.bindView(provideView())
        rootView = window.decorView.rootView.findViewById(rootContainerResId) as ViewGroup
        BaseApp.provideApp(this).noInternetCallback = this
    }

    protected abstract fun providePresenter(): T

    protected fun getText(editText: EditText): String {
        return editText.text.toString().trim { it <= ' ' }
    }

    protected abstract fun provideView(): V
    protected abstract fun initViews()
    protected abstract fun setFonts()

    override fun provideContext(): Context {
        return this
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNetworkConnectionChanged(isNetworkConnected: Boolean) {
        if (!isNetworkConnected) {
            showNoInternetConnectionToast()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
        setFonts()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unbindView()
        presenter?.onDestroy()
    }

    override fun showToast(message: String) {
        runOnUiThread { Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show() }
    }

    override fun showToast(@StringRes text: Int) {
        showToast(getString(text))
    }


    protected fun startProgressLoading(title: String, message: String) {
        dialog = ProgressDialog.show(this, title,
                message, false)
    }

    protected fun startProgressLoading() {
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog!!.setCancelable(false)
        dialog!!.show()

    }

    override fun startLoadingDialog() {
        stopLoadingDialog()
        runOnUiThread {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.start(supportFragmentManager, DIALOG_TAG, true)
            }
        }
    }

    override fun stopLoadingDialog() {
        runOnUiThread {
            if (!isFinishing) {
                loadingDialog?.dismissAllowingStateLoss()
                loadingDialog = null
            }
        }
    }

    protected fun setProgressValue(progress: Int) {
        dialog?.progress = progress
    }

    protected fun stopProgressLoading() {
        try {
            dialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun showAlertDialog(message: String) {
        if (alertDialog == null) {
            alertDialog = BaseAlertDialog.Builder(this, R.color.colorAccent)
                    .setPositiveButton(getString(R.string.ok), { dialog, _ -> dialog.dismiss() })
                    .setCancelable(false)
                    .create()
        }
        alertDialog?.setMessage(message)
        alertDialog?.show()
    }

    protected fun showNoNetworkConnectionSnackBarIfNeeded() {
        if (!NetworkChangeReceiver.isNetworkConnected(this)) {
            Handler().postDelayed({ showNoInternetConnectionToast() }, HANDLER_DELAY.toLong())
        }
    }

    private fun showNoInternetConnectionToast() {
        showToast(R.string.no_network_connection)
    }

    override fun goToLoginScreen() {
    }

    protected inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    protected inline fun <reified T : Activity> Activity.navigate(bundle: Bundle) {
        val intent = Intent(this, T::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    protected inline fun <reified T : Activity> Activity.navigateAndClear() {
        val intent = Intent(this, T::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {

        private val DIALOG_TAG = "dialog_tag"
    }


}
