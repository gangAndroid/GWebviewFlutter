package gang.gwebview.g_webview_flutter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gangAndroid on 2019/9/3.
 * @Github：https://github.com/gangAndroid
 */
public class X5WebViewActivityJ extends Activity {

    private X5WebView mWebView;

//    private RxPermissions rxPermissions;
//    private Disposable call;

    private String imgPathTemp = "";
    private ValueCallback<Uri> uploadMessage = null;
    private ValueCallback<Uri[]> uploadMessageAboveL = null;
    private AlertDialog mGenderDialog;
    private static final int FILE_CHOOSER_RESULT_CODE = 10000;
    private static final int REQUEST_CODE_CAMERA = 20000;

    private int type = 1;
    private String userInfoJson = "";
    private String userPhoneJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null)
            getActionBar().hide();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            WebView.enableSlowWholeDocumentDraw();
//        }
        setContentView(R.layout.main_x5_java);

        mWebView = findViewById(R.id.main_main_web);

//        rxPermissions = new RxPermissions(this);
//        call = rxPermissions.request(Manifest.permission.INTERNET,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
        init();
//                        } else {
//                            Toast.makeText(MainActivity.this, "由于没有获得授权，将不能运行", Toast.LENGTH_SHORT).show();
//                            MainActivity.this.finish();
//                        }
//                    }
//                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (call != null)
//            call.dispose();
//        call = null;
        mWebView.destroy();
    }

    private void init() {
        com.tencent.smtt.sdk.WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//        mWebView.setDrawingCacheEnabled(false);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//                view.loadUrl(url);
                if (!url.startsWith("http")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    Map<String, String> extraHeaders = new HashMap<String, String>();
//                    extraHeaders.put("Referer", _URL);
                    if (view != null) {
                        view.loadUrl(url, extraHeaders);
                    }
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
                Toast.makeText(X5WebViewActivityJ.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
//        mWebView.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent));
//        mWebView.setBackgroundResource(R.color.black);
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        if (getIntent().hasExtra("userInfo_key")) {
            type = 1;
            String userInfo_key = getIntent().getStringExtra("userInfo_key");
            String userInfo_phone = getIntent().getStringExtra("userInfo_phone");
            String userInfo_userType = getIntent().getStringExtra("userInfo_userType");
            String userInfo_nodeTime = getIntent().getStringExtra("userInfo_nodeTime");
            userInfoJson = "{\"userKey\":\"" + userInfo_key + "\",\"phone\":\"" + userInfo_phone + "\",\"userType\":\"" + userInfo_userType + "\",\"nodeTime\":\"" + userInfo_nodeTime + "\"}";
            userPhoneJson = "";
        } else {
            type = 2;
            userInfoJson = "";
            String userInfo_phone = getIntent().getStringExtra("userInfo_phone");
            userPhoneJson = "{\"phone\":\"" + userInfo_phone + "\"}";
        }

        mWebView.addJavascriptInterface(new MyJsInterface(userInfoJson, userPhoneJson), "Android");

    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @SuppressLint("JavascriptInterface")
    public class MyJsInterface {
        private String userInfoJson;
        private String userPhoneJson;

        public MyJsInterface(String userInfoJson, String userPhoneJson) {
            this.userInfoJson = userInfoJson;
            this.userPhoneJson = userPhoneJson;
        }

        @JavascriptInterface
        public String getUserInfo() {
            Log.e("getUserInfo-phone", "11111111" + this.userPhoneJson);
            Log.e("getUserInfo-info", "222222" + this.userInfoJson);
            if (this.userInfoJson.equals(""))
                return this.userPhoneJson;
            else
                return this.userInfoJson;
        }

        @JavascriptInterface
        public void closeWebView() {
            X5WebViewActivityJ.this.finish();
        }

        @JavascriptInterface
        public void createVCLWalletCallback(String json) {
            Log.e("createVCLWalletCallback", "" + json);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:
                if (null == uploadMessage && null == uploadMessageAboveL) return;
                Uri result = null;
                if (data == null || resultCode != Activity.RESULT_OK) {
                    result = null;
                } else {
                    result = data.getData();
                }
                if (uploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(result);
                    uploadMessage = null;
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (null == uploadMessage && null == uploadMessageAboveL) return;
                if (uploadMessageAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (uploadMessage != null) {
                    File file = new File(imgPathTemp);
                    Uri imageUri = Uri.fromFile(file);
                    uploadMessage.onReceiveValue(imageUri);
                    uploadMessage = null;
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == FILE_CHOOSER_RESULT_CODE && uploadMessageAboveL != null) {
        if (uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    private long mPressedTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            long mNowTime = System.currentTimeMillis();
            if ((mNowTime - mPressedTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            } else {//退出程序
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
