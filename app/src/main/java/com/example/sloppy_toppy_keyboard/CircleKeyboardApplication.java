package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class CircleKeyboardApplication extends InputMethodService {

    private InputConnection inputConnection;

    @Override
    public View onCreateInputView() {
        MainKeyboardView mainKeyboardView = new MainKeyboardView(this, this);
        // Should be able to just return mainKeyboardView, but keyboard doesn't render, so have to do it this way instead.
        // Think it has to do with the way the view is inflated.
        return mainKeyboardView.getKeyboardView();
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
        inputConnection.commitText(s, 1);

    }

    // Not 100% what all code here does but it works. Probably possibly can delete some of it as it was copied from elsewhere.
    public void deleteText() {
        CharSequence sel = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(sel)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 0);
        }
    }

    public void finishComposingText() {
        sendDownAndUpKeyEvent(KeyEvent.KEYCODE_ENTER, 0);
    }

    public void sendDownAndUpKeyEvent(int keyEventCode, int flags) {
        sendDownKeyEvent(keyEventCode, flags);
        sendUpKeyEvent(keyEventCode, flags);
    }

    public void sendDownKeyEvent(int keyEventCode, int flags) {
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

    public void sendUpKeyEvent(int keyEventCode, int flags) {
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
