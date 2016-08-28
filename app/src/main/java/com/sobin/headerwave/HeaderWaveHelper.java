package com.sobin.headerwave;

/**
 * Created by yuanshuobin on 16/8/26.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class HeaderWaveHelper {
    private HeaderWaveView mHeaderWaveView;
    private View mFloatView;
    private AnimatorSet mAnimatorSet;
    private float waterLevelAnimF, amplitudeAnimF;

    public HeaderWaveHelper(HeaderWaveView HeaderWaveView, View view) {
        mHeaderWaveView = HeaderWaveView;
        mFloatView = view;
        initAnimation();
    }

    public void start() {
        mHeaderWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();


        // vertical animation.
        // water level increases from 0 to center of HeaderWaveView
        final ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "waterLevelRatio", 0.8f, 0.8f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());


        // amplitude animation.
        // wave grows big then grows small, repeatedly
        final ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "amplitudeRatio", 0.05f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());


        // horizontal animation.
        // wave waves infinitely.
        final ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(2000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());


        waveShiftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float value = mHeaderWaveView.getSinHeight()-mFloatView.getMeasuredHeight();
                mFloatView.setRotation(-value);
                mFloatView.setTranslationY(value);
                mFloatView.invalidate();
            }
        });

        animators.add(waveShiftAnim);

        animators.add(amplitudeAnim);

        animators.add(waterLevelAnim);


        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
    }
}