package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

import androidx.core.view.MotionEventCompat;

public class CircleOnPressListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private InputConnection inputConnection;
    private boolean isLeftCircle;

    private String selectedLetter = "";
    private String prevSelectedLetter = "";
    private static final int circleActivationRange = 75;

    private int fingerIndex;

    public CircleOnPressListener(Context context, InputConnection inputConnection, boolean isLeftCircle) {
        this.context = context;
        this.inputConnection = inputConnection;
        this.isLeftCircle = isLeftCircle;
    }

    public View.OnTouchListener getButtonCallback(Button button) {
        return (view, motionEvent) -> {
//            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
//            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
//                // For the first pointer that touches the screen - Its index is always 0
//                Log.d(TAG, "ACTION_DOWN");
//                fingerIndex = 0;
//            } else if (fingerAction.equals(MotionEvent.ACTION_POINTER_DOWN)) {
//                // For extra pointers that enter the screen beyond the first. The index of the pointer that just went down can be obtained by using getActionIndex()
//                Log.d(TAG, "ACTION_POINTER_DOWN");
//                fingerIndex = 1;
//            } else if (fingerAction.equals(MotionEvent.ACTION_POINTER_UP)) {
//                // Sent when a non-primary pointer goes up. The index of the pointer that just went up can be obtained by using getActionIndex().
//                Log.d(TAG, "ACTION_POINTER_UP");
//                if (motionEvent.getActionIndex() == fingerIndex) {
//                    fingerIndex = -1;
//                }
//                return true;
//            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
//                // Sent when the last pointer leaves the screen.
//                Log.d(TAG, "ACTION_UP");
//                fingerIndex = -1;
//                return true;
//            }

            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                // For the first pointer that touches the screen - Its index is always 0
                Log.d(TAG, "ACTION_DOWN");
                fingerIndex = 0;
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                // Sent when the last pointer leaves the screen.
                Log.d(TAG, "ACTION_UP");
                fingerIndex = -1;
                return true;
            }

            // (0, 0) plus width and height halved to get center of button.
            Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            Vector2 touchRelativePos = new Vector2(motionEvent.getX(fingerIndex), motionEvent.getY(fingerIndex));   // Do we need fingerIndex now that each circle button is using its own class instance?
            double touchDistFromCenter = Math.hypot(buttonCenterPos.x - touchRelativePos.x, touchRelativePos.y - buttonCenterPos.y);

            Log.d(TAG, "getButtonCallback: " + touchDistFromCenter);

            // If touch is > than x distance away, get angle and insert that character.
            if (touchDistFromCenter >= circleActivationRange) {
                float angle = getAngle(buttonCenterPos, touchRelativePos);
                float range = 360 / (8 * 2);  // 8 letters times 2 to give range to left and right of each
//                Log.d(TAG, "onCreateInputView: angle: " + angle);

                if (isInRangeOfFirstLetter(angle, range)) {
                    selectedLetter = isLeftCircle ? "a" : "i";
                }
                else if (isInRange(angle, 45, range)) {
                    selectedLetter = isLeftCircle ? "b" : "j";
                }
                else if (isInRange(angle, 90, range)) {
                    selectedLetter = isLeftCircle ? "c" : "k";
                }
                else if (isInRange(angle, 135, range)) {
                    selectedLetter = isLeftCircle ? "d" : "l";
                }
                else if (isInRange(angle, 180, range)) {
                    selectedLetter = isLeftCircle ? "e" : "m";
                }
                else if (isInRange(angle, 225, range)) {
                    selectedLetter = isLeftCircle ? "f" : "n";
                }
                else if (isInRange(angle, 270, range)) {
                    selectedLetter = isLeftCircle ? "g" : "o";
                }
                else if (isInRange(angle, 315, range)) {
                    selectedLetter = isLeftCircle ? "h" : "p";
                }
                else {
                    //selectedLetter = "??";
                    // There are some very small deadzones between letters, but selectedLetter will just be set to the previous selectedLetter (if one was selected).
                    // ToDo: Move the above into a method (GetSelectedLetter()), and then call on it again from here with an increased range.
                }

                if (selectedLetter != "") {
                    button.setText(selectedLetter);
                }
            }
            else if (touchDistFromCenter < circleActivationRange && selectedLetter != "") {
                inputConnection.commitText(selectedLetter, 1);
                selectedLetter = "";
                button.setText("O");
            }

            //Log.d(TAG, "onCreateInputView: " + touchDistFromCenter);
            //Log.d(TAG, "onCreateInputView: " + touchVector.x + " " + touchVector.y);
            //Log.d(TAG, "onCreateInputView: " + buttonCenterPos.x + " " + buttonCenterPos.y);

            if (selectedLetter != prevSelectedLetter) {
//                vibrate();
            }
            prevSelectedLetter = selectedLetter;

            return true;
        };
    }

    private float getAngle(Vector2 centerPoint, Vector2 otherPoint) {
        float angle = (float) Math.toDegrees(Math.atan2(centerPoint.y - otherPoint.y, centerPoint.x - otherPoint.x));
        if (angle < 0) angle += 360;
        return angle;
    }

    // Returns true if actualAngle is withing range of angleToCheck.
    // (Example: actualAngle is 83, angleToCheck is 90, and range is 5, then it returns true because 83 is less than 5 units away from 90.)
    private boolean isInRange(float actualAngle, int angleToCheck, float range) {
        if (actualAngle > (angleToCheck - range) && actualAngle < (angleToCheck + range))
            return true;
        return false;
    }

    // actualAngle will never be less than 0 and never be greater than 360 (Pi).
    // So if checking for a range of 22.5, angle is in range if it is greater than 337.5 and less than 22.5.
    private boolean isInRangeOfFirstLetter(float actualAngle, float range) {
        if (actualAngle > (360 - range) || actualAngle < (0 + range))
            return true;
        return false;
    }

    private void vibrate() {
        Vibrator v = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (v == null) return;

        int milliseconds = 25;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }

}
