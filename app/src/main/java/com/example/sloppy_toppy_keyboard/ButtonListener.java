package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ButtonListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private MainKeyboardView mainKeyboardView;

    private String selectedLetter = "";
    private String prevSelectedLetter = "";
    private static final int firstRingActivationRange = 85;
    private static final int secondRingActivationRange = 200;


    public ButtonListener(Context context, CircleKeyboardApplication circleKeyboardApplication, MainKeyboardView mainKeyboardView) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.mainKeyboardView = mainKeyboardView;
    }

    public View.OnTouchListener getButtonCallback(Button button) {
        return (view, motionEvent) -> {
            Log.d(TAG, "CALLED");
            view.getHeight();
            view.performClick();
            return true;
        };
    }

}
