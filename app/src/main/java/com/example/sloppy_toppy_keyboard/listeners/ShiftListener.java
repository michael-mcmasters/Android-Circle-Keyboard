package com.example.sloppy_toppy_keyboard.listeners;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.keyboardViews.MainKeyboardView;

public class ShiftListener {

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private MainKeyboardView mainKeyboardView;
    private boolean upperCase;
    private long firstTapTimestamp;
    private long doubleTapTimeWindow;


    public ShiftListener(Context context, CircleKeyboardApplication circleKeyboardApplication, MainKeyboardView mainKeyboardView) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.mainKeyboardView = mainKeyboardView;
        this.upperCase = true;
        firstTapTimestamp = -1;
        doubleTapTimeWindow = 1000;
        mainKeyboardView.shift(upperCase);
    }

    // set caps lock always caps on double tap
    // otherwise, toggle
    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                boolean doubleTapped = System.currentTimeMillis() - firstTapTimestamp < doubleTapTimeWindow;
                firstTapTimestamp = System.currentTimeMillis();

                if (doubleTapped) {
                    // TODO if already caps lock, lowercase
                    Log.d("", "DOUBLE TAPPED");
                    circleKeyboardApplication.toggleShift(true);

                } else {
                    Log.d("", "SINGLE TAP");
                    circleKeyboardApplication.toggleShift(false);
                }


//                if (System.currentTimeMillis() - firstTapTimestamp < doubleTapTimeWindow) {
//                    firstTapTimestamp = -1;
//                } else {
//                    firstTapTimestamp = System.currentTimeMillis();
//                }

//                if (System.currentTimeMillis() - firstTapTimestamp < doubleTapTimeWindow) {
//                    Log.d("", "DOUBLE TAPPED");
//                    firstTapTimestamp = -1;
//                } else {
//                    firstTapTimestamp = System.currentTimeMillis();
//                }


            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

}
