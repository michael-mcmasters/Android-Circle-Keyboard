package com.example.sloppy_toppy_keyboard.keyboardViews;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.R;
import com.example.sloppy_toppy_keyboard.listeners.ShiftListener;
import com.example.sloppy_toppy_keyboard.model.KeyBindings;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.model.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
import com.example.sloppy_toppy_keyboard.listeners.EnterListener;
import com.example.sloppy_toppy_keyboard.listeners.NumListener;
import com.example.sloppy_toppy_keyboard.listeners.SpaceListener;
import com.example.sloppy_toppy_keyboard.old.CircleOnPressListener;

import java.io.IOException;
import java.io.InputStream;

public class CharactersKeyboardView extends ConstraintLayout {

    private static final String TAG = "CharactersKeyboardView";

    public CharactersKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        super(context);

//        buttonExample
//        Keyboard keyboard = new Keyboard(context, R.layout.characters_layout);
//        this.setKeyboard(keyboard);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.characters_layout, this);

        findViewById(R.id.buttonExample).setOnTouchListener((view, motionEvent) -> {
                Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
                if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                    circleKeyboardApplication.setViewMainKeyboard();
                }
                view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
                return true;
            }
        );
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
