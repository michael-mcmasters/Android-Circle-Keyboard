package com.example.sloppy_toppy_keyboard.listeners;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.model.Vector2;

public class ButtonListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private KeyMap keyMap;

    private Vector2 startPosition;
    private Vector2 endPosition;
    private boolean vibrated;


    public ButtonListener(Context context, CircleKeyboardApplication circleKeyboardApplication, KeyMap keyMap) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.keyMap = keyMap;
    }

    // Gets start position
    // Gets end position
    // Determines if up/down/left/right
    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                Log.d(TAG, "Touch Down");
                onTouchDown(motionEvent);
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                Log.d(TAG, "Touch Up");
                onTouchUp(view, motionEvent);
            }

            onTouchDrag(view, motionEvent);

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

    private void onTouchDown(MotionEvent motionEvent) {
        startPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));  // should be relative to the button
        vibrated = false;
    }

    private void onTouchDrag(View view, MotionEvent motionEvent) {
        if (!vibrated && getTouchDistanceFromCenter(view, motionEvent) > 50) {
            vibrate(10);
            vibrated = true;
        }

//        if (draggedOutsideOfButton(view, motionEvent)) {
//            vibrate(30);
//            vibrated = true;
//        }
    }

    private void onTouchUp(View view, MotionEvent motionEvent) {
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

    // Returns true if touch is greater than the button width / 2
    private boolean draggedOutsideOfButton(View view, MotionEvent motionEvent) {
        double touchDistFromCenter = getTouchDistanceFromCenter(view, motionEvent);
        return touchDistFromCenter > view.getWidth() / 2 || touchDistFromCenter > view.getHeight() / 2;
    }

    private double getTouchDistanceFromCenter(View view, MotionEvent motionEvent) {
        Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
        Vector2 touchPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));
        return Math.hypot(buttonCenterPos.x - touchPosition.x, touchPosition.y - buttonCenterPos.y);
    }

    private void vibrate(int milliseconds) {
        Vibrator v = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (v == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }

}
