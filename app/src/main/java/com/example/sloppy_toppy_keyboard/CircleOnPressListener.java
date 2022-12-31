package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.view.MotionEventCompat;

public class CircleOnPressListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private MainKeyboardView mainKeyboardView;
    private boolean isLeftCircle;

    private String selectedLetter = "";
    private String prevSelectedLetter = "";
    private static final int firstRingActivationRange = 100;
    private static final int secondRingActivationRange = 200;

    private int fingerIndex;

    public CircleOnPressListener(Context context, CircleKeyboardApplication circleKeyboardApplication, MainKeyboardView mainKeyboardView, boolean isLeftCircle) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.mainKeyboardView = mainKeyboardView;
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
                mainKeyboardView.notifyButtonState(isLeftCircle, "ACTION_DOWN");
                fingerIndex = 0;
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                // Sent when the last pointer leaves the screen.
                Log.d(TAG, "ACTION_UP");
                fingerIndex = -1;
                mainKeyboardView.notifyButtonState(isLeftCircle, "ACTION_UP");
                return true;
            }

            // (0, 0) plus width and height halved to get center of button.
            Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            Vector2 touchRelativePos = new Vector2(motionEvent.getX(fingerIndex), motionEvent.getY(fingerIndex));   // Do we need fingerIndex now that each circle button is using its own class instance?
            double touchDistFromCenter = Math.hypot(buttonCenterPos.x - touchRelativePos.x, touchRelativePos.y - buttonCenterPos.y);

            Log.d(TAG, "getButtonCallback: " + touchDistFromCenter);

            if (touchDistFromCenter >= firstRingActivationRange) {
                float touchAngleFromCenter = getAngle(buttonCenterPos, touchRelativePos);
                float angleRange = 360 / (8 * 2);  // Pie (circle) / 8 letters times 2 to give range to left and right of each

                if (touchDistFromCenter >= secondRingActivationRange) {
                    selectedLetter = detectSecondRingLetter(touchAngleFromCenter, angleRange);
                }
                selectedLetter = detectFirstRingLetter(selectedLetter, touchAngleFromCenter, angleRange);
            }
            else if (touchDistFromCenter < firstRingActivationRange && selectedLetter != "") {
                circleKeyboardApplication.commitText(selectedLetter);
                selectedLetter = "";
                button.setText("");
            }

            if (selectedLetter != prevSelectedLetter) {
                button.setText(selectedLetter);
                vibrate();
            }
            prevSelectedLetter = selectedLetter;

            return true;
        };
    }

    // Returns the second ring letter if one exists for the given angle
    private String detectSecondRingLetter(float angle, float range) {
        if (isLeftCircle) {
            if (isInRangeOfLeftMostLetter(angle, range)) {
                return "x";
            }
            else if (isInRange(angle, 45, range)) {
                return "y";
            }
            else if (isInRange(angle, 90, range)) {
                return "z";
            }
            else if (isInRange(angle, 270, range)) {
                return "v";
            }
            else if (isInRange(angle, 315, range)) {
                return "w";
            } else {
                return "";
            }
        } else {
            if (isInRange(angle, 90, range)) {
                return "q";
            }
            else if (isInRange(angle, 135, range)) {
                return "r";
            }
            else if (isInRange(angle, 180, range)) {
                return "s";
            }
            else if (isInRange(angle, 225, range)) {
                return "t";
            }
            else if (isInRange(angle, 270, range)) {
                return "u";
            } else {
                return "";
            }
        }
    }

    // Returns the first ring letter for the given angle. If a second ring letter is already selected for that angle, returns that instead.
    private String detectFirstRingLetter(String selectedLetter, float angle, float range) {
        if (isInRangeOfLeftMostLetter(angle, range)) {
            String firstRingLetter = isLeftCircle ? "a" : "i";
            if (selectedLetter != "x") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 45, range)) {
            String firstRingLetter = isLeftCircle ? "b" : "j";
            if (selectedLetter != "y") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 90, range)) {
            String firstRingLetter = isLeftCircle ? "c" : "k";
            if (selectedLetter != "z" && selectedLetter != "q") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 135, range)) {
            String firstRingLetter = isLeftCircle ? "d" : "l";
            if (selectedLetter != "r") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 180, range)) {
            String firstRingLetter = isLeftCircle ? "e" : "m";
            if (selectedLetter != "s") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 225, range)) {
            String firstRingLetter = isLeftCircle ? "f" : "n";
            if (selectedLetter != "t") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 270, range)) {
            String firstRingLetter = isLeftCircle ? "g" : "o";
            if (selectedLetter != "v" && selectedLetter != "u") {
                selectedLetter = firstRingLetter;
            }
        }
        else if (isInRange(angle, 315, range)) {
            String firstRingLetter = isLeftCircle ? "h" : "p";
            if (selectedLetter != "w") {
                selectedLetter = firstRingLetter;
            }
        }
        else {
            //selectedLetter = "??";
            // There are some very small deadzones between letters, but selectedLetter will just be set to the previous selectedLetter (if one was selected).
            // ToDo: Move the above into a method (GetSelectedLetter()), and then call on it again from here with an increased range.
        }
        return selectedLetter;
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

    // This function is used for the left letters instead of isInRange because actualAngle will never be less than 0 and never be greater than 360 (Pi).
    // Leftmost letter meaning any letter to the direct left of the circle.
    // So if checking for a range of 22.5, angle is in range if it is greater than 337.5 and less than 22.5.
    private boolean isInRangeOfLeftMostLetter(float actualAngle, float range) {
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

        int milliseconds = 5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }

}
