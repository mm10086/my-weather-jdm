package com.example.mm.app;

import com.example.mm.db.CityDB;
import com.example.mm.bean.City;

import android.os.Environment;
import android.app.Application;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by mm on 2017/11/1.
 */
//mini天气项目的application对象
public class MyApplication extends Application {
    private static final String TAG = "MyApp";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;
    @Override
    //application对象的onCreate函数，用于初始化
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"MyApplication->Oncreate");
        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }
    //初始化城市列表函数
    private void initCityList(){
        mCityList = new ArrayList<City>();
        //新建一个线程用于初始化城市列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
    //具体的获取城市信息的函数
    private boolean prepareCityList() {
        //调用CityDB类中的获取城市信息函数
        mCityList = mCityDB.getAllCity();
        int i=0;
        for (City city : mCityList) {
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }
    //从数据库中读取城市信息的函数
    private CityDB openCityDB() {
        //从路径中读取数据库文件
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            //如果路径不存在，在新建一个路径
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                //从数据库文件中读取城市信息
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
    //获取城市列表函数
    public List<City> getCityList() {
        return mCityList;
    }
    //获取application实例
    public static MyApplication getInstance(){
        return mApplication;
    }
}
