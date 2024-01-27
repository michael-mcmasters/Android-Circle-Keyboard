package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.model.KeyMap;

public class ButtonListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private KeyMap keyMap;

    private Vector2 startPosition;
    private Vector2 endPosition;


    public ButtonListener(Context context, CircleKeyboardApplication circleKeyboardApplication, KeyMap keyMap) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.keyMap = keyMap;
    }

    // Gets start position
    // Gets end position
    // Determines if up/down/left/right
    public View.OnTouchListener getButtonCallback(Button button) {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                Log.d(TAG, "Touch Down");
                onTouchDown(motionEvent);
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                Log.d(TAG, "Touch Up");
                onTouchUp(motionEvent);
            }

            view.performClick();
            return true;
        };
    }

    private void onTouchDown(MotionEvent motionEvent) {
        startPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));  // should be relative to the button
    }

    private void onTouchUp(MotionEvent motionEvent) {
        endPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));
        Log.d(TAG, String.format("startPosition x: %s, y: %s", startPosition.x, startPosition.y));
        Log.d(TAG, String.format("endPosition x: %s, y: %s", endPosition.x, endPosition.y));
        Log.d(TAG, "");

        double xDistance = Math.abs(startPosition.x - endPosition.x);
        double yDistance = Math.abs(startPosition.y - endPosition.y);

        // Determine if trail was longer left/right or up/down to determine which letter to prioritize. (Useful if moving diagonally.)
        if (xDistance > yDistance) {
            if (endPosition.x < startPosition.x) {
                // left
                circleKeyboardApplication.commitText(keyMap.getLeft());
            } else {
                // right
                circleKeyboardApplication.commitText(keyMap.getRight());
            }
        } else {
            if (endPosition.y < startPosition.y) {
                // up
                circleKeyboardApplication.commitText(keyMap.getUp());
            } else {
                // down
                circleKeyboardApplication.commitText(keyMap.getDown());
            }
        }
    }

}
