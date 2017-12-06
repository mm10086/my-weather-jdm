package com.example.mm.myweather;
import com.example.mm.app.MyApplication;
import com.example.mm.bean.City;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mm on 2017/10/18.
 */
//城市选择界面的Activity类
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private TextView mTitileName;
    private ListView mList;
    private EditText mEditText;
    private ArrayAdapter<String> mAdapter;
    private List<City> filterDataList;

    @Override
    //重写SelectCity的onCreate函数
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);
        //初始化城市选择界面
        initViews();
    }
    @Override
    //设置点击事件
    public void onClick(View view) {
        switch (view.getId()) {
            //如果点击了返回按钮，则向主界面传递包含城市编码信息的intent
            case R.id.title_back:
                Intent i = new Intent();
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cityCode = sharedPreferences.getString("main_city_code", "101010100");
                i.putExtra("cityCode", cityCode);
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }
    //初始化城市选择界面
    private void initViews() {
        //为mBackBtn设置监听事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        //更新城市选择界面标题栏的城市名称，从SharedPreference中读取数据
        mTitileName = (TextView)findViewById(R.id.title_name);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String cityName_tmp = sharedPreferences.getString("main_city_name", "北京");
        mTitileName.setText("当前城市：" + cityName_tmp);

        //建立ListView控件
        mList=(ListView)findViewById(R.id.title_list);

        //从MyApplication中获取城市列表
        MyApplication myApplication = MyApplication.getInstance();
        final List<City> cityList = myApplication.getCityList();
        setmList(cityList);
        //建立搜索框控件
        mEditText = (EditText)findViewById(R.id.search_edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterDataList = filterData(charSequence.toString(), cityList);
                setmList(filterDataList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setmList(final List<City> cityList) {
        List<String> cityName = new ArrayList<String>();
        for (City city : cityList) {
            cityName.add(city.getCity() + city.getNumber());
        }
        //为ListView设定数据和适配器，以及设定列表点击事件
        mAdapter = new ArrayAdapter<String>(SelectCity.this, android.R.layout.simple_list_item_1, cityName);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                City city = cityList.get(position);
                //当点击了某个城市条目后，同时更新SharedPreference里的当前城市信息
                SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("main_city_code", city.getNumber());
                editor.putString("main_city_name", city.getCity());
                editor.commit();
                //向主界面返回包含当前城市信息的intent
                Intent i = new Intent();
                i.putExtra("cityCode", city.getNumber());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private List<City> filterData(String filterStr, List<City> cityList) {
        filterDataList = new ArrayList<City>();
        Log.d("Filter", filterStr);
        if(TextUtils.isEmpty(filterStr)) {
            for(City city : cityList) {
                filterDataList.add(city);
            }
        }
        else {
            filterDataList.clear();
            for(City city : cityList) {
                if(city.getCity().indexOf(filterStr.toString()) != -1) {
                    filterDataList.add(city);
                }
            }
        }
        return filterDataList;
    }
}
