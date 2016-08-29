package com.sobin.headerwave;

/**
 * Created by yuanshuobin on 16/8/26.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class HeaderWaveHelper {
    private HeaderWaveView mHeaderWaveView;
    private View mFloatView;
    private AnimatorSet mAnimatorSet;
    private ObjectAnimator mAmplitudeAnim, amplitudeChangeAnim;
    private Handler mHandler = new Handler();
    private boolean mHasCancel = false;
    private boolean mHasStart = false;
    private boolean mIsFirst = false;

    public HeaderWaveHelper(HeaderWaveView HeaderWaveView, View view) {
        mHeaderWaveView = HeaderWaveView;
        mFloatView = view;
        initAnimation();
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
        mAmplitudeAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "amplitudeRatio", 0.05f, 0.05f);
        mAmplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        mAmplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        mAmplitudeAnim.setDuration(5000);
        mAmplitudeAnim.setInterpolator(new LinearInterpolator());


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
                //获取sin函数的height，更新mFloatView
                float value = mHeaderWaveView.getSinHeight() - mFloatView.getMeasuredHeight();
                mFloatView.setRotation(-value * 0.92f);
                mFloatView.setTranslationY(value);
                mFloatView.invalidate();
            }
        });

        animators.add(waveShiftAnim);

        animators.add(mAmplitudeAnim);

        animators.add(waterLevelAnim);


        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void start() {
        mHeaderWaveView.setShowWave(true);
        if (mAnimatorSet != null && !mHasStart) {
            mHasStart = true;
            mHasCancel = false;
            if (amplitudeChangeAnim != null) {
                amplitudeChangeAnim = ObjectAnimator.ofFloat(
                        mHeaderWaveView, "amplitudeRatio", 0.00001f, 0.05f);
                amplitudeChangeAnim.setDuration(1000);
                amplitudeChangeAnim.start();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mIsFirst) {
                        mAnimatorSet.start();
                        mIsFirst = true;
                    }

                }
            }, 1000);

        }
    }

    public void cancel() {
        if (mAnimatorSet != null && !mHasCancel) {
            mHasCancel = true;
            mHasStart = false;
            mAmplitudeAnim.cancel();
            amplitudeChangeAnim = ObjectAnimator.ofFloat(
                    mHeaderWaveView, "amplitudeRatio", 0.05f, 0.00001f);
            amplitudeChangeAnim.setDuration(1000);
            amplitudeChangeAnim.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mAnimatorSet.cancel();
                    if (mFloatView.getAnimation() != null) {
                        mFloatView.getAnimation().cancel();
                    }
                }
            }, 1000);
        }


    }
}