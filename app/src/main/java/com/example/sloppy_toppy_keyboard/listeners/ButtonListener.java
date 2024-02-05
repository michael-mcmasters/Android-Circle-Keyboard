package com.example.sloppy_toppy_keyboard.listeners;

import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_LEFT;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_LEFT_WORD;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_RIGHT;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_RIGHT_WORD;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.enums.KeyboardArrowDirection;
import com.example.sloppy_toppy_keyboard.keyboardViews.MainKeyboardView;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.model.Vector2;

public class ButtonListener {

    private static final String TAG = "CircleOnPressListener";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private MainKeyboardView mainKeyboardView;
    private KeyMap keyMap;

    private Vector2 startPosition;
    private Vector2 endPosition;

    private boolean vibrated;
    private boolean vibratedFarLetter;
    private boolean doubleTappedForFarLetter;

    private boolean slidOutAndInForFarLetter;
    private boolean hitFirstLetter;
    private String selectedOutAndInLetter = "";

    private double prevDistance;


    public ButtonListener(Context context, CircleKeyboardApplication circleKeyboardApplication, MainKeyboardView mainKeyboardView, KeyMap keyMap) {
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        this.mainKeyboardView = mainKeyboardView;
        this.keyMap = keyMap;
    }

    // Gets start position
    // Gets end position
    // Determines if up/down/left/right
    public View.OnTouchListener getButtonCallback() {
        return (view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                onTouchDown(motionEvent);
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                onTouchUp(view, motionEvent);
            }

            endPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));
            onTouchDrag(view, motionEvent);

            prevDistance = getTouchDistanceFromStartPoint(view, motionEvent);
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        };
    }

    private void onTouchDown(MotionEvent motionEvent) {
        startPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));  // should be relative to the button
        vibrated = false;
        vibratedFarLetter = false;
        slidOutAndInForFarLetter = false;
        hitFirstLetter = false;
    }

    // Check line boundaries (if x <= 10 || y <= 10) ... (this may not work)
    //

    private void onTouchDrag(View view, MotionEvent motionEvent) {
        double xDistance = Math.abs(startPosition.x - endPosition.x);
        double yDistance = Math.abs(startPosition.y - endPosition.y);
        if (!vibrated && getTouchDistanceFromStartPoint(view, motionEvent) > 50) {
            vibrate(10);
            vibrated = true;
            hitFirstLetter = true;


            // Determine if trail was longer left/right or up/down to determine which letter to prioritize. (Useful if moving diagonally.)
            if (xDistance > yDistance) {
                if (endPosition.x < startPosition.x) {
                    selectedOutAndInLetter = keyMap.getFarLeft();
                } else {
                    selectedOutAndInLetter = keyMap.getFarRight();
                }
            } else {
                if (endPosition.y < startPosition.y) {
                    selectedOutAndInLetter = keyMap.getFarUp();
                } else {
                    selectedOutAndInLetter = keyMap.getFarDown();
                }
            }
        }


//        if (hitFirstLetter && !slidOutAndInForFarLetter && getTouchDistanceFromStartPoint(view, motionEvent) < 50) {
        if (hitFirstLetter && !slidOutAndInForFarLetter && getTouchDistanceFromStartPoint(view, motionEvent) < prevDistance && !draggedOutsideOfButton(view, motionEvent)) {
            vibrate(10);
            slidOutAndInForFarLetter = true;
//            selectedOutAndInLetter = "";

//            double xDistance = Math.abs(startPosition.x - endPosition.x);
//            double yDistance = Math.abs(startPosition.y - endPosition.y);

//            if (slidOutAndInForFarLetter) {
//                // Determine if trail was longer left/right or up/down to determine which letter to prioritize. (Useful if moving diagonally.)
//                if (xDistance > yDistance) {
//                    if (endPosition.x < startPosition.x) {
//                        selectedOutAndInLetter = keyMap.getFarLeft();
//                    } else {
//                        selectedOutAndInLetter = keyMap.getFarRight();
//                    }
//                } else {
//                    if (endPosition.y < startPosition.y) {
//                        selectedOutAndInLetter = keyMap.getFarUp();
//                    } else {
//                        selectedOutAndInLetter = keyMap.getFarDown();
//                    }
//                }
//            }
        }
//        if (!vibratedFarLetter && getTouchDistanceFromStartPoint(view, motionEvent) > 250) {
//            vibrate(10);
//            vibratedFarLetter = true;
//        }
    }

    private void onTouchUp(View view, MotionEvent motionEvent) {
        Log.d(TAG, String.format("startPosition x: %s, y: %s", startPosition.x, startPosition.y));
        Log.d(TAG, String.format("endPosition x: %s, y: %s", endPosition.x, endPosition.y));
        Log.d(TAG, "");

        boolean selectedFarLetter = false;      // not sure if I need this or not

        if (getTouchDistanceFromStartPoint(view, motionEvent) < 10) {
            mainKeyboardView.performTapAction(keyMap.getTap());

//            String tapAction = keyMap.getTap();
//            int highlightCursorStartPosition = -1; // Set this when mod key is held down to highlight text. -1 means not highlighting
//
//            if (tapAction.equals(CURSOR_LEFT)) {
//                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.LEFT, false, highlightCursorStartPosition);
//            } else if (tapAction.equals(CURSOR_RIGHT)) {
//                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.RIGHT, false, highlightCursorStartPosition);
//            } else if (tapAction.equals(CURSOR_LEFT_WORD)) {
//                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.LEFT, true, highlightCursorStartPosition);
//            } else if (tapAction.equals(CURSOR_RIGHT_WORD)) {
//                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.RIGHT, true, highlightCursorStartPosition);
//            }

            return;
        }

        double xDistance = Math.abs(startPosition.x - endPosition.x);
        double yDistance = Math.abs(startPosition.y - endPosition.y);

        if (slidOutAndInForFarLetter) {
            circleKeyboardApplication.commitText(selectedOutAndInLetter);
        }
        else {
            // Determine if trail was longer left/right or up/down to determine which letter to prioritize. (Useful if moving diagonally.)
            if (xDistance > yDistance) {
                if (endPosition.x < startPosition.x) {
                    // left
                    circleKeyboardApplication.commitText(!selectedFarLetter ? keyMap.getLeft() : keyMap.getFarLeft());

                } else {
                    // right
                    circleKeyboardApplication.commitText(!selectedFarLetter ? keyMap.getRight() : keyMap.getFarRight());
                }
            } else {
                if (endPosition.y < startPosition.y) {
                    // up
                    circleKeyboardApplication.commitText(!selectedFarLetter ? keyMap.getUp() : keyMap.getFarUp());
                } else {
                    // down
                    circleKeyboardApplication.commitText(!selectedFarLetter ? keyMap.getDown() : keyMap.getFarDown());
                }
            }
        }

        doubleTappedForFarLetter = false;
    }

    // Returns true if touch is greater than the button width / 2
    private boolean draggedOutsideOfButton(View view, MotionEvent motionEvent) {
        double touchDistFromCenter = getTouchDistanceFromCenter(view, motionEvent);
        return touchDistFromCenter > view.getWidth() / 2 || touchDistFromCenter > view.getHeight() / 2;
    }

    private double getTouchDistanceFromStartPoint(View view, MotionEvent motionEvent) {
        Vector2 currentPosition = new Vector2(motionEvent.getX(0), motionEvent.getY(0));
        return Math.hypot(startPosition.x - currentPosition.x, currentPosition.y - startPosition.y);
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
