package com.basekotlin.app.presentation


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import com.basekotlin.app.R


class LoadingDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    companion object {

        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }

        fun start(manager: FragmentManager, dialogTag: String, isCancelable: Boolean): LoadingDialog {
            val dialog = LoadingDialog.newInstance()
            dialog.isCancelable = isCancelable
            val transaction = manager.beginTransaction()
            transaction.add(dialog, dialogTag)
            transaction.commitAllowingStateLoss()
            return dialog
        }
    }

}
