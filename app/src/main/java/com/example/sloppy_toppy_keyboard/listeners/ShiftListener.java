package com.example.sloppy_toppy_keyboard.listeners;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.MainKeyboardView;

public class ShiftListener {

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private MainKeyboardView mainKeyboardView;
    private boolean upperCase;


    public ShiftListener(Context context, CircleKeyboardApplication circleKeyboardApplication, MainKeyboardView mainKeyboardView) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.mainKeyboardView = mainKeyboardView;
        this.upperCase = false;
    }

    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                upperCase = !upperCase;
                mainKeyboardView.shift(upperCase);
            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

}
