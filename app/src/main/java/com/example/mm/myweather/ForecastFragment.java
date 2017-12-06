package com.example.mm.myweather;
import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.example.mm.bean.TodayWeather;
import com.example.mm.myweather.ViewPagerAdapter;
import com.example.mm.bean.Forcast;
import com.example.mm.util.SetWeatherImage;
/**
 * Created by mm on 2017/11/30.
 */

public class ForecastFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private PagerAdapter adapter;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.dot1, R.id.dot2};
    private View rootView;
    private TodayWeather weather;
    private TextView day1,day2,day3,day4,temp1,temp2,temp3,temp4,type1,type2,type3,type4,feng1,feng2,feng3,feng4;
    private ImageView weatherImg1,weatherImg2,weatherImg3,weatherImg4;
    private ViewPager vp;
    public ForecastFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.weather_forecast, container, false);
        initViews();
        initIndicator();
        //setForcastInfo(); //null pointer
        return rootView;
    }

    public void setWeather(TodayWeather weather){
        this.weather = weather;
    }

    public void setForcastInfo(){
        Forcast f = null;
        View view = views.get(0);
        f = weather.getForcastArr()[0];
        day1 = (TextView) view.findViewById(R.id.forcast_day_1);
        weatherImg1 = (ImageView)view.findViewById(R.id.forcast_img_1);
        temp1 = (TextView) view.findViewById(R.id.forcast_temp_1);
        type1 = (TextView) view.findViewById(R.id.forcast_type_1);
        feng1 = (TextView) view.findViewById(R.id.forcast_feng_1);

        day1.setText(f.getDate());
        weatherImg1.setImageResource(SetWeatherImage.setImageByType(f.getType()));
        temp1.setText(f.getTempString());
        type1.setText(f.getType());
        feng1.setText(f.getFengli());

        f = weather.getForcastArr()[1];
        day2 = (TextView) view.findViewById(R.id.forcast_day_2);
        weatherImg2 = (ImageView)view.findViewById(R.id.forcast_img_2);
        temp2 = (TextView) view.findViewById(R.id.forcast_temp_2);
        type2 = (TextView) view.findViewById(R.id.forcast_type_2);
        feng2 = (TextView) view.findViewById(R.id.forcast_feng_2);
        day2.setText(f.getDate());
        weatherImg2.setImageResource(SetWeatherImage.setImageByType(f.getType()));
        temp2.setText(f.getTempString());
        type2.setText(f.getType());
        feng2.setText(f.getFengli());

        f = weather.getForcastArr()[2];
        day3 = (TextView) view.findViewById(R.id.forcast_day_3);
        weatherImg3 = (ImageView)view.findViewById(R.id.forcast_img_3);
        temp3 = (TextView) view.findViewById(R.id.forcast_temp_3);
        type3 = (TextView) view.findViewById(R.id.forcast_type_3);
        feng3 = (TextView) view.findViewById(R.id.forcast_feng_3);

        day3.setText(f.getDate());
        weatherImg3.setImageResource(SetWeatherImage.setImageByType(f.getType()));
        temp3.setText(f.getTempString());
        type3.setText(f.getType());
        feng3.setText(f.getFengli());

        view = views.get(1);
        f = weather.getForcastArr()[3];
        day4 = (TextView) view.findViewById(R.id.forcast_day_4);
        weatherImg4 = (ImageView)view.findViewById(R.id.forcast_img_4);

        temp4 = (TextView) view.findViewById(R.id.forcast_temp_4);
        type4 = (TextView) view.findViewById(R.id.forcast_type_4);
        feng4 = (TextView) view.findViewById(R.id.forcast_feng_4);
        day4.setText(f.getDate());
        weatherImg4.setImageResource(SetWeatherImage.setImageByType(f.getType()));
        temp4.setText(f.getTempString());
        type4.setText(f.getType());
        feng4.setText(f.getFengli());

        Log.i("ForcastFragment","Update success!");
    }

    private void initIndicator(){
        dots = new ImageView[views.size()];
        for(int i = 0; i < views.size(); i++){
            dots[i] = (ImageView)rootView.findViewById(ids[i]);
        }
    }

    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.pager_forcast1,null));
        views.add(inflater.inflate(R.layout.pager_forcast2,null));
        adapter = new ViewPagerAdapter(views,this.getActivity());
        vp = (ViewPager)rootView.findViewById(R.id.forcast_pager);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        for(int i = 0; i < ids.length; i++){
            if(position == i){
                dots[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                dots[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
        adapter = vp.getAdapter();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}