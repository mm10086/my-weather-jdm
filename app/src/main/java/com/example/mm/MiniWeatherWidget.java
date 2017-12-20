package com.example.mm;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.mm.myweather.R;
import com.example.mm.service.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class MiniWeatherWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {   //第一次添加小组件时调用的方法
        super.onEnabled(context);

        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);                                   //开启获取天气的服务
    }

    @Override
    public void onDisabled(Context context) {    //最后一个小组件被移除时调用的方法
        super.onDisabled(context);

        Intent intent = new Intent(context,WidgetService.class);
        context.stopService(intent);                                   //关闭服务
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {    //更新小组件，这里我们用服务更新小组件，所以该方法不用添加代码
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

