package com.redblack.taksim.ui.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.redblack.taksim.R;
import com.redblack.taksim.adapters.ViewPagerAdapter;
import com.redblack.taksim.ui.logintype.MainType;
import com.redblack.taksim.utils.PreferenceLoginSession;
import com.redblack.taksim.utils.PreferenceManager;


public class ViewPager extends AppCompatActivity implements View.OnClickListener {

    private android.support.v4.view.ViewPager mPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide,R.layout.fourth_slide};
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton next,back;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private ImageButton btn_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //Check of Intro Slider is show or not
/*         if(new PreferenceManager(ViewPager.this).checkPreference()){ //it should be

            loadHome();
        }*/

        //Check of Logined System or Logout System
        if(new PreferenceLoginSession(ViewPager.this).checkPreference()){

            loadHome();
        }

             setContentView(R.layout.view_pager);


     //initialize variable
        next = findViewById(R.id.btn_next);
        back = findViewById(R.id.btn_back);
        mPager = findViewById(R.id.viewpager);
        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);
        btn_start = findViewById(R.id.start_btn);

     //Button ClickListener
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        btn_start.setOnClickListener(this);

        next.setVisibility(View.VISIBLE);

     //initializa viewPager
        viewPagerAdapter = new ViewPagerAdapter(layouts,this);

        mPager.setAdapter(viewPagerAdapter);

        mPager.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

             if(i == 0){

                 back.setVisibility(View.INVISIBLE);

             }else if(i != 0){

                 back.setVisibility(View.VISIBLE);
             }

             createDots(i);
             if(i == layouts.length-1){

                 btn_start.setVisibility(View.VISIBLE);
                 next.setVisibility(View.INVISIBLE);
             }else{
                 next.setVisibility(View.VISIBLE);
                 btn_start.setVisibility(View.INVISIBLE);
             }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_next:
                loadNextSlide();
                break;

            case R.id.btn_back:
                loadBackSlide();
                break;

            case R.id.start_btn:

                loadHome();
                //Save sharedPreferences of intro slider showed
                new PreferenceManager(ViewPager.this).writePreference();

                break;
        }

    }

    private void createDots(int current_position){

       if(Dots_Layout != null)
           Dots_Layout.removeAllViews();

           dots = new ImageView[layouts.length];

           for(int i = 0;i<layouts.length; i++){

               dots[i] = new ImageView(this);
               if(i==current_position){

                   dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
               }else{
                   dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
               }

               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                       ViewGroup.LayoutParams.WRAP_CONTENT);
               params.setMargins(4,0,4,0);

               Dots_Layout.addView(dots[i],params);
           }

    }

    private void loadHome(){

        startActivity(new Intent(this,MainType.class));

        finish();
    }

    private void loadNextSlide(){

        int next_slide = mPager.getCurrentItem()+1;

        if(next_slide<layouts.length){

            mPager.setCurrentItem(next_slide);
        }else{
            loadHome();

        }
    }

    private void loadBackSlide(){

        int back_slide = mPager.getCurrentItem()-1;

        if(back_slide<layouts.length){
            mPager.setCurrentItem(back_slide);
        }

    }
}
