package com.basekotlin.app.presentation.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.basekotlin.app.R
import com.basekotlin.app.presentation.Presenter
import com.basekotlin.app.presentation.View


abstract class BaseFragment<T : Presenter<V>, V : View> : BaseChildFragment(), View {

    protected var presenter: T? = null

    protected abstract val layoutRes: Int
    private var alertDialog: AlertDialog? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = providePresenter()
        if (presenter != null) {
            presenter!!.bindView(provideView())
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater!!.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    protected abstract fun initViews(view: android.view.View?)

    protected abstract fun providePresenter(): T

    protected abstract fun provideView(): V

    override fun onStart() {
        super.onStart()
        if (presenter != null) {
            presenter!!.onStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if (presenter != null) {
            presenter!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (presenter != null) {
            presenter!!.onPause()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (presenter != null) {
            presenter!!.onSaveInstanceState(outState!!)
        }
    }

    override fun onStop() {
        super.onStop()
        if (presenter != null) {
            presenter!!.onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) {
            presenter!!.unbindView()
            presenter!!.onDestroy()
        }
    }

    override fun showToast(message: String) {
        activity.runOnUiThread { Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
    }

    override fun showToast(@StringRes text: Int) {
        showToast(getString(text))
    }

    override fun provideContext(): Context {
        return activity
    }


    override fun startLoadingDialog() {
        activity.runOnUiThread { (activity as BaseActivity<*, *>).startLoadingDialog() }
    }

    override fun stopLoadingDialog() {
        activity.runOnUiThread { (activity as BaseActivity<*, *>).stopLoadingDialog() }
    }


    override fun showAlertDialog(message: String) {
        if (alertDialog == null) {
            alertDialog = BaseAlertDialog.Builder(activity, R.color.colorAccent)
                    .setPositiveButton(getString(R.string.ok), { dialog, _ -> dialog.dismiss() })
                    .setCancelable(false)
                    .create()
        }
        alertDialog?.setMessage(message)
        alertDialog?.show()
    }

    override fun goToLoginScreen() {
    }

    private inline fun <reified T : Activity> Activity.navigateAndClear() {
        val intent = Intent(this, T::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}
