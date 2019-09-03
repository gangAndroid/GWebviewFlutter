import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:g_webview_flutter/g_webview_flutter.dart';

void main() {
  GWebviewFlutter.init(); // 初始化SDK
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // js代码如：
    String jsStr = "";
    jsStr = "javascript:function modifyText() {";
    jsStr += "var list=document.getElementById(\"header\");";
    jsStr += "var str=list.getElementsByTagName(\"div\");";
    jsStr += "for(var i=0;i<str.length;i++){";
    jsStr += "if(str[i].className==\"topclose\")";
    jsStr += "str[i].style.display=\"none\";";
    jsStr += "}";
    jsStr += "}";
    jsStr += "javascript:modifyText();";
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
            child: Text('Running on: '),
            onTap: () {
              GWebviewFlutter.openWebActivity("https://mn.vpubchain.com/ben/#/account",
                  title: "标题",
                  jsLoaded: jsStr,
                  is4: true,
                  userInfo_key: "",
                  userInfo_phone: "18620627664",
                  userInfo_userType: "1",
                  userInfo_nodeTime: "1");
            },
          ),
        ),
      ),
    );
  }
}
