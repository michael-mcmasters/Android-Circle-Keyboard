package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.example.sloppy_toppy_keyboard.enums.KeyboardView;
import com.example.sloppy_toppy_keyboard.keyboardViews.CharactersKeyboardView;
import com.example.sloppy_toppy_keyboard.keyboardViews.MainKeyboardView;

public class CircleKeyboardApplication extends InputMethodService {

    private MainKeyboardView mainKeyboardView;
    private CharactersKeyboardView charactersKeyboardView;

    private InputConnection inputConnection;
    private boolean shiftEnabled;


    @Override
    public View onCreateInputView() {
        mainKeyboardView = new MainKeyboardView(this, this);
        charactersKeyboardView = new CharactersKeyboardView(this, this);
        return mainKeyboardView;
    }

    public void changeKeyboardView(KeyboardView keyboardView) {
        switch (keyboardView) {
            case MAIN_KEYBOARD:
                setInputView(mainKeyboardView);
                break;
            case CHARACTERS_KEYBOARD:
                setInputView(charactersKeyboardView);
                break;
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        inputConnection = getCurrentInputConnection();
    }

    @Override
    public void onBindInput() {
        inputConnection = getCurrentInputConnection();
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        inputConnection = getCurrentInputConnection();
    }

    // May be a good idea to move this to its own TextCommitter class in the future
    public void commitText(String s) {
        if (s.length() == 1 && Character.isLetter(s.charAt(0))) {
            s = String.valueOf(shiftEnabled ? Character.toUpperCase(s.charAt(0)) : Character.toLowerCase(s.charAt(0)));
        }
        inputConnection.commitText(s, 1);
    }

    // Not 100% what all code here does but it works. Probably possibly can delete some of it as it was copied from elsewhere.
    public void backspace() {
        CharSequence sel = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(sel)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 0);
        }
    }

    public void enter() {
        sendDownAndUpKeyEvent(KeyEvent.KEYCODE_ENTER, 0);
    }

    public void shift(boolean enabled) {
        Log.d("", "Parent Shift");
        shiftEnabled = enabled;

    }

    private void sendDownAndUpKeyEvent(int keyEventCode, int flags) {
        sendDownKeyEvent(keyEventCode, flags);
        sendUpKeyEvent(keyEventCode, flags);
    }

    private void sendDownKeyEvent(int keyEventCode, int flags) {
        inputConnection.sendKeyEvent(
                new KeyEvent(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        KeyEvent.ACTION_DOWN,
                        keyEventCode,
                        0,
                        flags
                )
        );
    }

    private void sendUpKeyEvent(int keyEventCode, int flags) {
        inputConnection.sendKeyEvent(
                new KeyEvent(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        KeyEvent.ACTION_UP,
                        keyEventCode,
                        0,
                        flags
                )
        );
    }
}
