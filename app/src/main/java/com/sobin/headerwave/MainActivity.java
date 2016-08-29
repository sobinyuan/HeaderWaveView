package com.sobin.headerwave;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    private HeaderWaveHelper mHeaderWaveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeaderWaveView waveView = (HeaderWaveView) findViewById(R.id.header_wave_view);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.sv);


        waveView.setWaveColor(
                Color.parseColor("#80FC7A8C"),
                Color.parseColor("#40FB3D53"));
        mHeaderWaveHelper = new HeaderWaveHelper(waveView, imageView);

        //SDK API23以下请自行继承ScrollView实现该方法。
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                if (mScrollView.getScrollY() > 85) {
                    mHeaderWaveHelper.cancel();
                } else {
                    mHeaderWaveHelper.start();
                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeaderWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHeaderWaveHelper.start();
    }


}
