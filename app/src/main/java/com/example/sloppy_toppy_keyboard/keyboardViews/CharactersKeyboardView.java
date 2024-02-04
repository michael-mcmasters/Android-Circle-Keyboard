package com.example.sloppy_toppy_keyboard.keyboardViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.R;
import com.example.sloppy_toppy_keyboard.enums.KeyboardArrowDirection;
import com.example.sloppy_toppy_keyboard.enums.KeyboardView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

public class CharactersKeyboardView extends ConstraintLayout {

    private static final String TAG = "CharactersKeyboardView";

    private boolean ctrlHeld;
    private boolean highlightHeld;
    private int highlightCursorStartPosition;


    public CharactersKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        super(context);
        ctrlHeld = false;
        highlightHeld = false;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.characters_layout_2, this);

        findViewById(R.id.homeButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.moveCursorWithHomeButton(highlightCursorStartPosition);
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.leftButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.LEFT, ctrlHeld, highlightCursorStartPosition);
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.rightButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.moveCursorWithArrowButton(KeyboardArrowDirection.RIGHT, ctrlHeld, highlightCursorStartPosition);
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.endButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.moveCursorWithEndButton(highlightCursorStartPosition);
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.ctrlButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                ctrlHeld = true;
            }
            else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                ctrlHeld = false;
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.bothButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                ctrlHeld = true;
                highlightHeld = true;
                highlightCursorStartPosition = circleKeyboardApplication.getCursorPosition();
            }
            else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                ctrlHeld = false;
                highlightHeld = false;
                highlightCursorStartPosition = -1;
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.shiftButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                highlightHeld = true;
                highlightCursorStartPosition = circleKeyboardApplication.getCursorPosition();
            }
            else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                highlightHeld = false;
                highlightCursorStartPosition = -1;
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

        findViewById(R.id.mainKeyboardButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                circleKeyboardApplication.changeKeyboardView(KeyboardView.MAIN_KEYBOARD);
            }
            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });

//        findViewById(R.id.highlightButton).setOnTouchListener((view, motionEvent) -> {
//            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
//            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
//                circleKeyboardApplication.highlight();
//            }
//            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
//            return true;
//        });
    }


//    public MainKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
//        super(context);
//        this.context = context;
//        this.circleKeyboardApplication = circleKeyboardApplication;
//        leftCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, true);
//        rightCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, false);
//
//        leftCircleState = "ACTION_UP";
//        rightCircleState = "ACTION_UP";
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.key_layout, this);
//
//        KeyBindings keyBindings = getKeyMapFromConfigFile(R.raw.key_bindings);
//        setLettersVisually(keyBindings);
//        setLettersFunctionally(keyBindings);
//    }

}
