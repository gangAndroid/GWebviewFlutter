package gang.gwebview.g_webview_flutter

import io.flutter.plugin.common.EventChannel
import android.app.Activity
import io.flutter.plugin.common.PluginRegistry

/**
 * @author gangAndroid on 2019/9/2.
 * @Github：https://github.com/gangAndroid
 */
class GWebViewCall() : EventChannel.StreamHandler {
    var CHANNEL = "g_webview_flutter"
    var channel: EventChannel? = null
    var listener: EventChannel.EventSink?=null

    fun registerWith(registrar: PluginRegistry.Registrar) {
        channel = EventChannel(registrar.messenger(), CHANNEL)
        val instance = GWebViewCall()
        channel!!.setStreamHandler(instance)
    }

    override fun onListen(p0: Any?, p1: EventChannel.EventSink?) {
        listener = p1
    }

    override fun onCancel(p0: Any?) {
    }
}