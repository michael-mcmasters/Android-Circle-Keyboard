package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainKeyboardView extends ConstraintLayout {

    private static String TAG = "MyIMService";
    private Context context;
    private InputConnection inputConnection;
    private View myKeyboardView;

    private String selectedLetter = "";
    private String prevSelectedLetter = "";
    private static final int circleActivationRange = 75;

    public MainKeyboardView(Context context, InputConnection inputConnection) {
        super(context);
        this.context = context;
        this.inputConnection = inputConnection;
        myKeyboardView = initialize();
    }

    public View getMyKeyboardView() {
        return myKeyboardView;
    }

    private View initialize() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myKeyboardView = inflater.inflate(R.layout.key_layout, null);

        // Trying to make a TextView for every letter, that only pops up when user is hovering over that letter.
        TextView text = myKeyboardView.findViewById(R.id.aKey);

        Button button0 = myKeyboardView.findViewById(R.id.button0);
        Button button1 = myKeyboardView.findViewById(R.id.button1);

        int btnHeight = 300;
        button0.getLayoutParams().height = btnHeight;
        button0.getLayoutParams().width = btnHeight;
        button1.getLayoutParams().height = btnHeight;
        button1.getLayoutParams().width = btnHeight;

        View.OnTouchListener onTouchListenerCallback = (view, motion) -> {
            // (0, 0) plus width and height halved to get center of button.
            Vector2 buttonCenterPos = new Vector2(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            Vector2 touchRelativePos = new Vector2(motion.getX(), motion.getY());
            double touchDistFromCenter = Math.hypot(buttonCenterPos.x - touchRelativePos.x, touchRelativePos.y - buttonCenterPos.y);

            // If touch is > than x distance away, get angle and insert that character.
            if (touchDistFromCenter >= circleActivationRange) {
                float angle = getAngle(buttonCenterPos, touchRelativePos);
                float range = 360 / (8 * 2);  // 8 letters times 2 to give range to left and right of each
                Log.d(TAG, "onCreateInputView: angle: " + angle);

                if (isInRangeOfFirstLetter(angle, range)) {
                    selectedLetter = "g";
                    text.setVisibility(View.VISIBLE);
//                    vibrate();
                }
                else if (isInRange(angle, 45, range)) {
                    selectedLetter = "h";
                }
                else if (isInRange(angle, 90, range)) {
                    selectedLetter = "a";
                }
                else if (isInRange(angle, 135, range)) {
                    selectedLetter = "b";
                }
                else if (isInRange(angle, 180, range)) {
                    selectedLetter = "c";
                }
                else if (isInRange(angle, 225, range)) {
                    selectedLetter = "d";
                }
                else if (isInRange(angle, 270, range)) {
                    selectedLetter = "e";
                }
                else if (isInRange(angle, 315, range)) {
                    selectedLetter = "f";
                }
                else {
                    //selectedLetter = "??";
                    // There are some very small deadzones between letters, but selectedLetter will just be set to the previous selectedLetter (if one was selected).
                    // ToDo: Move the above into a method (GetSelectedLetter()), and then call on it again from here with an increased range.
                }

                if (selectedLetter != "") {
                    button1.setText(selectedLetter);
                }
            }
            else if (touchDistFromCenter < circleActivationRange && selectedLetter != "") {
                inputConnection.commitText(selectedLetter, 1);
                selectedLetter = "";
                button1.setText("O");
            }

            //Log.d(TAG, "onCreateInputView: " + touchDistFromCenter);
            //Log.d(TAG, "onCreateInputView: " + touchVector.x + " " + touchVector.y);
            //Log.d(TAG, "onCreateInputView: " + buttonCenterPos.x + " " + buttonCenterPos.y);

            if (selectedLetter != prevSelectedLetter) {
                vibrate();
            }
            prevSelectedLetter = selectedLetter;

            return true;
        };
        button0.setOnTouchListener(onTouchListenerCallback);
        button1.setOnTouchListener(onTouchListenerCallback);

        return myKeyboardView;
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

        int milliseconds = 25;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }

}
