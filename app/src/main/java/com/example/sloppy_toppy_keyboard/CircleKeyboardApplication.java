package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class CircleKeyboardApplication extends InputMethodService {

    @Override
    public View onCreateInputView() {
        InputConnection inputConnection = getCurrentInputConnection();
        MainKeyboardView mainKeyboardView = new MainKeyboardView(this, inputConnection);
        // Should be able to just return mainKeyboardView, but keyboard doesn't render, so have to do it this way instead.
        // Think it has to do with the way the view is inflated.
        return mainKeyboardView.getKeyboardView();
    }
}
