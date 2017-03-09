/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.test.hybird;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.apache.cordova.*;
import org.apache.cordova.engine.SystemWebView;

public class MainActivity extends CordovaActivity {

    /**插件开发4个步骤:
    1 在assents 目录下的  cordova-plugins.js文件添加配置

    2 在assets/www 的 plugin目录下编写javascript接口

    3 在res/xml 目录下配置  config.xml 文件
     添加<feature></feature> 有點像activity的Manifest

    4 在src目录下编写java文件 实现跳转*/

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        // Set by <content src="index.html" /> in config.xml
//        super.init();

//        loadUrl(launchUrl);
        loadUrl("file:///android_asset/www/InputData.html");

    }
}
