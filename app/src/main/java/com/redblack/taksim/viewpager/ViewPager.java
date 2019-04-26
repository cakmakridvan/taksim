package com.redblack.taksim.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.redblack.taksim.R;
import com.redblack.taksim.adapters.ViewPagerAdapter;

public class ViewPager extends AppCompatActivity {

    private android.support.v4.view.ViewPager mPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide,R.layout.fourth_slide};
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

     //initialize variable
        mPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(layouts,this);

        mPager.setAdapter(viewPagerAdapter);
    }
}
