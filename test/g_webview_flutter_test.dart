import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:g_webview_flutter/g_webview_flutter.dart';

void main() {
  const MethodChannel channel = MethodChannel('g_webview_flutter');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await GWebviewFlutter.platformVersion, '42');
  });
}
