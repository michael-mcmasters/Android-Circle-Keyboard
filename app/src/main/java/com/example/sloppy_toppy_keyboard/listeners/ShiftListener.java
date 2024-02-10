package com.example.sloppy_toppy_keyboard.listeners;

import static com.example.sloppy_toppy_keyboard.enums.ShiftState.LOWERCASE;
import static com.example.sloppy_toppy_keyboard.enums.ShiftState.UPPERCASE_ALWAYS;
import static com.example.sloppy_toppy_keyboard.enums.ShiftState.UPPERCASE_ONCE;

import android.content.Context;
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
        doubleTapTimeWindow = 700;
    }

    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                boolean doubleTapped = System.currentTimeMillis() - firstTapTimestamp < doubleTapTimeWindow;
                if (doubleTapped) {
                    handleDoubleTap();
                } else {
                    handleTap();
                }

                firstTapTimestamp = System.currentTimeMillis();
            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

    /**
     * Makes shift always be uppercase, unless it already is, in which it goes to lowercase because this may be a triple tap
     */
    public void handleDoubleTap() {
        if (circleKeyboardApplication.getShiftState() == UPPERCASE_ALWAYS) {
            circleKeyboardApplication.toggleShiftViaButton(LOWERCASE);
        } else {
            circleKeyboardApplication.toggleShiftViaButton(UPPERCASE_ALWAYS);
        }
    }

    public void handleTap() {
        switch (circleKeyboardApplication.getShiftState()) {
            case UPPERCASE_ALWAYS:
            case UPPERCASE_ONCE:
                circleKeyboardApplication.toggleShiftViaButton(LOWERCASE);
                break;
            case LOWERCASE:
                circleKeyboardApplication.toggleShiftViaButton(UPPERCASE_ONCE);
                break;
        }
    }

}
