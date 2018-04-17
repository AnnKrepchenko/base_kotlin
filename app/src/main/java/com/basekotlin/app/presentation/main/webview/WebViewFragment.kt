package com.basekotlin.app.presentation.main.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.basekotlin.app.BuildConfig
import com.basekotlin.app.R
import com.basekotlin.app.core.BaseApp
import com.basekotlin.app.presentation.base.BaseFragment
import com.basekotlin.app.utils.FileUtils
import com.basekotlin.app.utils.WebViewUtils
import com.basekotlin.app.utils.keyboard.KeyboardHeightObserver
import com.basekotlin.app.utils.keyboard.KeyboardHeightProvider
import kotlinx.android.synthetic.main.fragment_web_view.*
import java.io.File


/**
 * Created by ann on 2/27/18.
 */


class WebViewFragment : BaseFragment<WebViewPresenter<WebViewView>, WebViewView>(), WebViewView, KeyboardHeightObserver, CustomWebView.Listener {

    private val jsCallbackName = "url_changed_callback.js"
    private val jsKeyboardName = "keyboard_scroll.js"
    private val jsCallbackOnclick = "onclick_callback.js"

    private var isWebViewAvailable: Boolean = false
    private lateinit var pbLoader: ProgressBar
    private var webViewFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var keyboardHeightProvider: KeyboardHeightProvider? = null


    var position: Int? = null
    var uuid: String? = null
        set(value) {
            if (field == value) {
                // Don't refresh, it wont change anything
                return
            }
            if (TextUtils.isEmpty(cwvMainWeb?.url)) {
                presenter?.load(value)
            }
            field = value
        }
    var urlJsInterface: URLChangedInterface? = null

    //TODO 10.04.18 now it is useless, remove or use it later
    private var keyboardHeightPx = 400

    override val layoutRes: Int
        get() = R.layout.fragment_web_view

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews(view: View?) {
        isWebViewAvailable = true
        cwvMainWeb = view!!.findViewById(R.id.cwvMainWebView)
        pbLoader = view.findViewById(R.id.pbMainWebLoader)
        cwvMainWeb?.setCookiesEnabled(true)
        cwvMainWeb?.webChromeClient = ZeroChromeClient()
        cwvMainWeb?.setListener(this@WebViewFragment, this, REQUEST_CODE_CUSTOM_FILE_PICKER)
        cwvMainWeb?.addJavascriptInterface(URLJvaScriptInterface(), JS_INTERFACE)
        cwvMainWeb?.addPermittedHostname(BuildConfig.WEB_BASE_URL)

        root.post { keyboardHeightProvider?.start() }

    }

    override fun providePresenter(): WebViewPresenter<WebViewView> {
        return com.basekotlin.app.presentation.main.webview.WebViewPresenterImpl(BaseApp.provideApp(activity))
    }

    override fun provideView(): WebViewView {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            uuid = savedInstanceState.getString(EXTRA_UUID)
        }
        keyboardHeightProvider = KeyboardHeightProvider(activity)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(EXTRA_UUID, uuid)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_FILE_PICKER -> cwvMainWeb?.onActivityResult(requestCode, resultCode, data)
            REQUEST_CODE_CUSTOM_FILE_PICKER -> {
                if (resultCode != Activity.RESULT_OK) {
                    webViewFilePathCallback?.onReceiveValue(null)
                    webViewFilePathCallback = null
                    return
                }

                val result = data?.dataString?.let { Uri.parse(it) } ?: getTempCameraUri()
                webViewFilePathCallback?.onReceiveValue(arrayOf(result))
                webViewFilePathCallback = null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        cwvMainWeb?.onPause()
        keyboardHeightProvider?.setKeyboardHeightObserver(null)
    }

    override fun onResume() {
        cwvMainWeb?.onResume()
        presenter?.getCookie()
        keyboardHeightProvider?.setKeyboardHeightObserver(this)
        super.onResume()
    }

    override fun loadWebView(url: String) {
        cwvMainWeb?.loadUrl(url)

    }

    override fun attachCookie(token: String) {
        WebViewUtils.attachCoockies(CookieManager.getInstance(), token)
    }

    override fun startLoadingDialog() {
        pbLoader.visibility = View.VISIBLE
    }

    override fun stopLoadingDialog() {
        pbLoader.visibility = View.GONE
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    override fun onDestroyView() {
        cwvMainWeb?.setListener(null as Fragment?, null)

        webViewFilePathCallback?.onReceiveValue(null)
        webViewFilePathCallback = null

        isWebViewAvailable = false
        super.onDestroyView()
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    override fun onDestroy() {
        cwvMainWeb?.removeJavascriptInterface(JS_INTERFACE)
        cwvMainWeb?.destroy()
        keyboardHeightProvider?.close()
        super.onDestroy()
    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        keyboardHeightPx = height
    }


    override fun onJsAttach() {
        Log.d("method", "onJsAttached")
        cwvMainWeb?.evaluateJavascript(FileUtils.loadAssetTextAsString(this@WebViewFragment.context, jsCallbackName), null)
        cwvMainWeb?.evaluateJavascript(FileUtils.loadAssetTextAsString(this@WebViewFragment.context, jsKeyboardName), null)
    }

    override fun onPageFinished(url: String?) {
        Log.d("method", "onPageFinished")
        stopLoadingDialog()
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        startLoadingDialog()
        Log.d("method", "onPageStarted")
    }

    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {
        Log.d("method", "onPageError")
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
        Log.d("method", "onDownloadRequested")
    }

    override fun onExternalPageRequest(url: String?) {
        Log.d("method", "onExternalPageRequest")
        urlJsInterface?.handleOtherUrl(url)
    }

    inner class ZeroChromeClient : WebChromeClient() {

        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams): Boolean {
            // FIXME: It would be nice if [fileChooserParams.acceptedTypes] actually contained something, so we
            // can show camera option only for image types.
            webViewFilePathCallback?.onReceiveValue(null)
            webViewFilePathCallback = filePathCallback

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .takeIf {
                        it.resolveActivity(context!!.packageManager) != null
                    }
                    ?.let {
                        val photoFile: File
                        try {
                            photoFile = createTempImageFile()
                        } catch (e: Throwable) {
                            Log.e(TAG, "Unable to create temporary image file", e)
                            return@let null
                        }

                        val uri = getTempCameraUri(photoFile)
                        return@let it.apply {
                            putExtra(MediaStore.EXTRA_OUTPUT, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        }
                    }
            // Fall back to default file chooser cause we can't provide camera
            // option
                    ?: return false

            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*" // FIXME: It would be nice if [fileChooserParams.acceptedTypes] actually contained something... see issue above
            }

            val chooserIntent = Intent(Intent.ACTION_CHOOSER).apply {
                putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
            }

            startActivityForResult(chooserIntent, REQUEST_CODE_CUSTOM_FILE_PICKER) // I owe you
            return true
        }

        private fun createTempImageFile(): File {
            return getTempCameraFile().apply { createNewFile() }
        }

    }

    private fun getTempCameraUri(file: File = getTempCameraFile()): Uri {
        val authority = "${context!!.packageName}.provider"
        return FileProvider.getUriForFile(context, authority, file)
    }

    private fun getTempCameraFile(): File {
        val storageDir = context!!.getExternalFilesDir("tmp")
        return File(storageDir, "temp.jpg")
    }

    companion object {
        val EXTRA_UUID = "uuid"
        private const val JS_INTERFACE = "android"
        var cwvMainWeb: CustomWebView? = null
        private const val TAG = "WebViewFragment"

        private const val REQUEST_CODE_FILE_PICKER = 1256
        private const val REQUEST_CODE_CUSTOM_FILE_PICKER = 1257

    }

    inner class URLJvaScriptInterface {
        @JavascriptInterface
        @MainThread
        fun onUrlChange(url: String) {
            activity.runOnUiThread { urlJsInterface?.onUrlChange(url) }
        }

        @MainThread
        @JavascriptInterface
        fun calculateVerticalScroll(y: Int): Int {
            val density = activity.resources.displayMetrics.density
            val windowHeightLimit = activity.window.decorView.height - 400 * density
            val windowY = y * density + intArrayOf(0, 0)
                    .apply {
                        cwvMainWeb?.getLocationInWindow(this)
                    }[1]
            return Math.max((windowY - windowHeightLimit) / density, 0f).toInt()
        }
    }
}
