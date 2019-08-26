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
                val intent = Intent(activity, H5Activity::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                intent.putExtra("js_loaded", jsLoaded)
                intent.putExtra("isShowUrl", isShowUrl)
                activity.startActivity(intent)
                result.success(true)
            }
        }
    }
}
