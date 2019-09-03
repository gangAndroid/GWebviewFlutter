package gang.gwebview.g_webview_flutter

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class GWebviewFlutterPlugin(var context: Context, var activity: Activity) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "g_webview_flutter")
            channel.setMethodCallHandler(GWebviewFlutterPlugin(registrar.context(), registrar.activity()))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
//    if (call.method == "getPlatformVersion") {
//      result.success("Android ${android.os.Build.VERSION.RELEASE}")
//    } else {
//      result.notImplemented()
//    }
        when (call.method) {
            "init" -> {
                result.success(true)
            }
            "openH5Activity" -> {
                // 地址
                val url = call.argument<String>("url")
                // 标题
                val title = call.argument<String>("title")
                // js代码
                val jsLoaded = call.argument<String>("js_loaded")
                // 是否显示地址栏
                val isShowUrl = call.argument<Boolean>("isShowUrl")

//                val intent = Intent(activity, H5Activity::class.java)
                val intent = Intent(activity, X5WebViewActivityJ::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                intent.putExtra("js_loaded", jsLoaded)
                val is4 = call.argument<Boolean>("is4")
                val userInfo_phone = call.argument<String>("userInfo_phone")
                intent.putExtra("userInfo_phone", userInfo_phone)
                if (is4 != null && is4 == true) {
                    val userInfo_key = call.argument<String>("userInfo_key")
                    val userInfo_userType = call.argument<String>("userInfo_userType")
                    val userInfo_nodeTime = call.argument<String>("userInfo_nodeTime")
                    intent.putExtra("userInfo_key", userInfo_key)
                    intent.putExtra("userInfo_userType", userInfo_userType)
                    intent.putExtra("userInfo_nodeTime", userInfo_nodeTime)
                }
                activity.startActivity(intent)
                result.success(true)
            }
        }
    }
}
