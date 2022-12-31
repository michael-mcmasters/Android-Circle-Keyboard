package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class CircleKeyboardApplication extends InputMethodService {

//    private InputConnection inputConnection;
    private MainKeyboardView mainKeyboardView;

    @Override
    public View onCreateInputView() {
        InputConnection inputConnection = getCurrentInputConnection();
        this.mainKeyboardView = new MainKeyboardView(this, inputConnection);
        // Should be able to just return mainKeyboardView, but keyboard doesn't render, so have to do it this way instead.
        // Think it has to do with the way the view is inflated.
        return mainKeyboardView.getKeyboardView();
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        if (mainKeyboardView != null) {
            mainKeyboardView.setInputConnection(getCurrentInputConnection());
        }
    }

    @Override
    public void onBindInput() {
        if (mainKeyboardView != null) {
            mainKeyboardView.setInputConnection(getCurrentInputConnection());
        }
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        if (mainKeyboardView != null) {
            mainKeyboardView.setInputConnection(getCurrentInputConnection());
        }
    }
}
