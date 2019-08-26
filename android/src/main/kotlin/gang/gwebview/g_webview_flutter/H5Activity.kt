package gang.gwebview.g_webview_flutter

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*

/**
 * @author gangAndroid on 2019/8/24.
 * @Github：https://github.com/gangAndroid
 */
class H5Activity : Activity() {
    lateinit var webView: WebView
    lateinit var mainPro: ProgressBar
    var closeView: ImageView? = null
    var mUrlView: EditText? = null
    var mClearView: ImageView? = null
    var mOkView: TextView? = null
    var mBackView: ImageView? = null
    var mGoView: ImageView? = null
    var mUploadMessage: ValueCallback<Uri>? = null
    var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null
    private var js_loaded: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        webView = findViewById(R.id.main_web)
        mainPro = findViewById(R.id.main_pro)
        closeView = findViewById(R.id.main_close)
        mUrlView = findViewById(R.id.main_url)
        mClearView = findViewById(R.id.main_clear)
        mOkView = findViewById(R.id.main_ok)
        mBackView = findViewById(R.id.main_back)
        mGoView = findViewById(R.id.main_go)

        initView()

        initListener()
    }

    private fun initListener() {
        mClearView!!.setOnClickListener {
            mClearView!!.visibility = View.GONE
            mUrlView!!.setText("")
        }
//        mUrlView!!.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                if (mClearView!!.visibility != View.VISIBLE) {
//                    if (s.toString().isNotEmpty()) {
//                        mClearView!!.visibility = View.VISIBLE
//                    }
//                }
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
        mUrlView!!.setOnClickListener {
            if (mClearView!!.visibility != View.VISIBLE) {
                mClearView!!.visibility = View.VISIBLE
            }
        }
        mOkView!!.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val v = window.peekDecorView()
            if (v != null) {
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            mOkView!!.requestFocus()
            mClearView!!.visibility = View.GONE
            val url = mUrlView!!.text.toString()
            if (url.isNotEmpty()) {
                if (!url.startsWith("http")) {
                    webView.loadUrl("http://$url")
                } else {
                    webView.loadUrl(url)
                }
            }
        }
        mBackView!!.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }
        mGoView!!.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }
    }

    private fun initView() {
        actionBar?.hide()
        closeView!!.setOnClickListener { this.finish() }
        title = intent.getStringExtra("title") ?: ""
        js_loaded = intent.getStringExtra("js_loaded") ?: ""
        val isShowUrl = intent.getBooleanExtra("isShowUrl", false)
        if (isShowUrl) {
            mUrlView!!.visibility = View.VISIBLE
            mOkView!!.visibility = View.VISIBLE
        }
        mClearView!!.visibility = View.GONE
        webView.apply {
            //            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            loadUrl(intent.getStringExtra("url"))
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
//                    return super.shouldOverrideUrlLoading(view, url)
                    view.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (js_loaded.isNotEmpty()) {
                        webView.loadUrl(js_loaded)
                    }
                    if (url != null && url.startsWith("http"))
                        mUrlView!!.setText(url)
                }
            }
            webChromeClient = object : WebChromeClient() {
                // For Android < 3.0
                fun openFileChooser(valueCallback: ValueCallback<Uri>) {
                    mUploadMessage = valueCallback
                    choosePicture(2)
                }

                // For Android  >= 3.0
                fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String) {
                    mUploadMessage = valueCallback
                    choosePicture(2)
                }

                //For Android  >= 4.1
                fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String, capture: String) {
                    mUploadMessage = valueCallback
                    choosePicture(2)
                }

                // For Android >= 5.0
                override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                    uploadMessageAboveL = filePathCallback
                    choosePicture(1)
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        mainPro.visibility = View.GONE
                    } else {
                        if (mainPro.visibility == View.GONE)
                            mainPro.visibility = View.VISIBLE
                        mainPro.progress = newProgress
                    }
                    super.onProgressChanged(view, newProgress)
                }
            }
        }
    }

    private fun choosePicture(type: Int) {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        this.startActivityForResult(Intent.createChooser(i, "选择图片"), type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        var result: Uri? = null
        if (data == null) {
            uploadMessageAboveL!!.onReceiveValue(null)
            uploadMessageAboveL = null
            mUploadMessage!!.onReceiveValue(null)
            mUploadMessage = null
            return
        } else {
            result = data.data
        }
        when (requestCode) {
            1 -> {
                onActivityResultAboveL(requestCode, resultCode, data)
            }
            2 -> {
                if (null == mUploadMessage) return
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        if (uploadMessageAboveL != null) {
            onActivityResultAboveL(requestCode, resultCode, data);
        } else if (mUploadMessage != null) {
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (uploadMessageAboveL == null)
            return
        var results: Array<Uri?>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    results = arrayOfNulls(clipData.itemCount)
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        if (results == null)
            return
        var count = 0
        for (i in 0 until results.size) {
            if (results[i] != null) {
                count += 1
            }
        }
        if (count == 0) {
            return
        }
        if (results[0] == null) {
            return
        }
        val results1: Array<Uri> = arrayOf(results[0]!!)
        uploadMessageAboveL!!.onReceiveValue(results1)
        uploadMessageAboveL = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        webView?.destroy()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }
}