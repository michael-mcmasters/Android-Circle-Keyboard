package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

// Naming it this because followed a tutorial and AndroidManifest.xml and perhaps other xml files may reference it (not sure, doing this quickly).
// In future, want to rename it to SloppyToppyKeyboardApplication or something.
public class MyIMServices2 extends InputMethodService {

    @Override
    public View onCreateInputView() {
        InputConnection inputConnection = getCurrentInputConnection();
        MainKeyboardView mainKeyboardView = new MainKeyboardView(this, inputConnection);
        // Should be able to just return mainKeyboardView, but keyboard doesn't render, so have to do it this way instead.
        // Think it has to do with the way the view is inflated.
        return mainKeyboardView.getMyKeyboardView();
    }
}
