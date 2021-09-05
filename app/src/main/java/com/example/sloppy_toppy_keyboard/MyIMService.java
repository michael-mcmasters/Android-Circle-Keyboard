package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyIMService extends InputMethodService implements View.OnClickListener {

    private static String TAG = "MyIMService";

    private String selectedLetter = "";

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

        // Trying to make a TextView for every letter, that only pops up when user is hovering over that letter.
        TextView text = myKeyboardView.findViewById(R.id.textView);

        Button button0 = myKeyboardView.findViewById(R.id.button0);
        Button button1 = myKeyboardView.findViewById(R.id.button1);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);

        InputConnection inputConnection = getCurrentInputConnection();

        View.OnTouchListener onTouchListenerCallback = (view, motion) -> {
                                          // (0, 0) plus width and height halved to get center of button.
            Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            Vector2 touchRelativePos = new Vector2(motion.getX(), motion.getY());
            double touchDistFromCenter = Math.hypot(buttonCenterPos.x - touchRelativePos.x, touchRelativePos.y - buttonCenterPos.y);

            // If touch is > than x distance away, get angle and insert that character.
            if (touchDistFromCenter > 40) {
                float angle = getAngle(buttonCenterPos, touchRelativePos);

                // Not all angles are checked so it may feel janky.
                if (isInRange(angle, 0)) {
                    selectedLetter = "g";
                    text.setVisibility(View.VISIBLE);
                }
                else if (isInRange(angle, 45)) {
                    selectedLetter = "h";
                }
                else if (isInRange(angle, 90)) {
                    selectedLetter = "a";
                }
                else if (isInRange(angle, 135)) {
                    selectedLetter = "b";
                }
                else if (isInRange(angle, 180)) {
                    selectedLetter = "c";
                }
                else if (isInRange(angle, 225)) {
                    selectedLetter = "d";
                }
                else if (isInRange(angle, 270)) {
                    selectedLetter = "e";
                }
                else if (isInRange(angle, 315)) {
                    selectedLetter = "f";
                }
            }
            else if (touchDistFromCenter < 30 && selectedLetter != "") {
                inputConnection.commitText(selectedLetter, 1);
                selectedLetter = "";
            }

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

    private float getAngle(Vector2 centerPoint, Vector2 otherPoint) {
        float angle = (float) Math.toDegrees(Math.atan2(centerPoint.y - otherPoint.y, centerPoint.x - otherPoint.x));
        if (angle < 0)
            angle += 360;

        return angle;
    }

    private boolean isInRange(float angle, int range) {
        if (angle > range - 10 && angle < range + 10)
            return true;
        return false;
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
