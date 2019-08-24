import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class GWebviewFlutter {
  static const MethodChannel _channel = const MethodChannel('g_webview_flutter');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> init() async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      bool res = await _channel.invokeMethod("init");
      return res;
    } else {
      return false;
    }
  }

  static Future<void> openWebActivity(String url, {String title, String js_loaded}) async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      final Map<String, dynamic> params = <String, dynamic>{
        'url': url,
        'title': title,
        'js_loaded': js_loaded
      };
      return await _channel.invokeMethod("openH5Activity", params);
    } else {
      return;
    }
  }
}
