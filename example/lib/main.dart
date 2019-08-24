import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:g_webview_flutter/g_webview_flutter.dart';

void main() {
  GWebviewFlutter.init();
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
              GWebviewFlutter.openWebActivity("http://pet.zoosnet.net/LR/Chatpre.aspx?lng=cn&id=PET83076163",
                  title: "标题", js_loaded: jsStr);
            },
          ),
        ),
      ),
    );
  }
}
