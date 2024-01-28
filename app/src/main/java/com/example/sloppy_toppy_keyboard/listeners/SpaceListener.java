package com.example.sloppy_toppy_keyboard.listeners;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;

public class SpaceListener {

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;


    public SpaceListener(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
    }

    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.backspace();
            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

}
