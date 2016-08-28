package com.sobin.headerwave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private HeaderWaveHelper mHeaderWaveHelper;
    HeaderWaveView waveView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waveView = (HeaderWaveView) findViewById(R.id.header_wave_view);
        imageView = (ImageView) findViewById(R.id.imageView);
//        waveView.setWaveColor(
//                Color.parseColor("#28f16d7a"),
//                Color.parseColor("#3cf16d7a"));
        mHeaderWaveHelper = new HeaderWaveHelper(waveView, imageView);
    }

    public void btnStart(View v) {

//        mHeaderWaveHelper.start();
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
