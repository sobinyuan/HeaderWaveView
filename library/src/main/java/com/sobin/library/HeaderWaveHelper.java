package com.sobin.library;

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
    private ObjectAnimator mAmplitudeAnim, amplitudeChangeAnim;
    private boolean mHasCancel = false;
    private boolean mHasStart = false;
    private boolean mIsFirst = false;
    //水位高低 waterLevelRatio
    private float mDefaultWaterLevelRatioF = 0.8f;
    private float mDefaultWaterLevelRatioT = 0.8f;
    //波浪大小振幅 amplitudeRatio
    private float mDefaultAmplitudeRatioF = 0.05f;
    private float mDefaultAmplitudeRatioT = 0.05f;
    //floatView 旋转调整角
    private float mDefaultFloatViewRotation = 85;

    public void setDefaultWaterLevelRatio(float defaultWaterLevelRatioF,float defaultWaterLevelRatioT) {
        mDefaultWaterLevelRatioF = defaultWaterLevelRatioF;
        mDefaultWaterLevelRatioT = defaultWaterLevelRatioT;
    }

    public void setDefaultAmplitudeRatio(float defaultAmplitudeRatioF,float defaultAmplitudeRatioT) {
        mDefaultAmplitudeRatioF = defaultAmplitudeRatioF;
        mDefaultAmplitudeRatioT = defaultAmplitudeRatioT;
    }

    public void setDefaultFloatViewRotation(float defaultFloatViewRotation) {
        mDefaultFloatViewRotation = defaultFloatViewRotation;
    }

    public HeaderWaveHelper(HeaderWaveView HeaderWaveView, int behindWaveColor, int frontWaveColor, View view) {
        mHeaderWaveView = HeaderWaveView;
        mFloatView = view;
        mHeaderWaveView.setWaveColor(behindWaveColor,frontWaveColor);
        initAnimation();
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();
        // vertical animation.
        // water level increases from 0 to center of HeaderWaveView
        final ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "waterLevelRatio", mDefaultWaterLevelRatioF, mDefaultWaterLevelRatioT);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());


        // amplitude animation.
        // wave grows big then grows small, repeatedly
        mAmplitudeAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "amplitudeRatio", mDefaultAmplitudeRatioF, mDefaultAmplitudeRatioT);
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
                mFloatView.setRotation(value + mDefaultFloatViewRotation);
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
                        mHeaderWaveView, "amplitudeRatio", 0.00001f, mDefaultAmplitudeRatioF);
                amplitudeChangeAnim.setDuration(1000);
                amplitudeChangeAnim.start();
            }

            if (!mIsFirst) {
                mAnimatorSet.start();
                mIsFirst = true;
            }


        }
    }

    public void cancel() {
        if (mAnimatorSet != null && !mHasCancel) {
            mHasCancel = true;
            mHasStart = false;
            mAmplitudeAnim.cancel();
            amplitudeChangeAnim = ObjectAnimator.ofFloat(
                    mHeaderWaveView, "amplitudeRatio", mDefaultAmplitudeRatioT, 0.00001f);
            amplitudeChangeAnim.setDuration(1000);
            amplitudeChangeAnim.start();
            amplitudeChangeAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mFloatView.getAnimation() != null) {
                        mFloatView.getAnimation().cancel();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }


    }
}