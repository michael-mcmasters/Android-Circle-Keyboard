package com.example.sloppy_toppy_keyboard;

import android.graphics.Point;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class MyIMService extends InputMethodService implements View.OnClickListener {

    private static String TAG = "MyIMService";

    private static class Vector2 {
        private double x;
        private double y;

        public Vector2 (float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public View onCreateInputView() {
        View myKeyboardView = getLayoutInflater().inflate(R.layout.key_layout, null);

        Button button0 = myKeyboardView.findViewById(R.id.button0);
        button0.setOnClickListener(this);

        Button button1 = myKeyboardView.findViewById(R.id.button1);
        button1.setOnClickListener(this);

        // On touch, get finger's current position, get dot rotation from center, that is the key being pressed.

        InputConnection ic = getCurrentInputConnection();

        View.OnTouchListener onTouchListenerCallback = (view, motion) -> {
            motion.getOrientation();

            // (0, 0) plus width and height halved to get center of button.
            Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            Vector2 touchRelativePos = new Vector2(motion.getX(), motion.getY());
            double touchDistFromCenter = Math.hypot(buttonCenterPos.x - touchRelativePos.x, touchRelativePos.y - buttonCenterPos.y);

            // Plan: If touch is > than x distance away, get angle and insert that character.

//            if (touchDistFromCenter > -40 && touchDistFromCenter < 40) {
//                ic.commitText("hiya", 1);
//            }
            Log.d(TAG, "onCreateInputView: " + touchDistFromCenter);
            //Log.d(TAG, "onCreateInputView: " + touchVector.x + " " + touchVector.y);
            //Log.d(TAG, "onCreateInputView: " + buttonCenterPos.x + " " + buttonCenterPos.y);

            float angle = getAngle(buttonCenterPos, touchRelativePos);
            Log.d(TAG, "onCreateInputView: Relative Pos: " + angle);

            return true;
        };

        button0.setOnTouchListener(onTouchListenerCallback);
        button1.setOnTouchListener(onTouchListenerCallback);
        return myKeyboardView;
    }

    private float getAngle(Vector2 target, Vector2 other) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - other.y, target.x - other.x));
        if(angle < 0)
            angle += 360;

        return angle;
    }

    @Override
    public void onClick(View v) {
        //handle all the keyboard key clicks here

        InputConnection ic = getCurrentInputConnection();
        if (v instanceof Button) {
            String clickedKeyText = ((Button) v).getText().toString();
            //ic.commitText(clickedKeyText, 1);
            ic.commitText("hiya", 1);
        }
    }
}
