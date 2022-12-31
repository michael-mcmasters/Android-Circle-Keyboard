package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
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
}
