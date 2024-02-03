package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.example.sloppy_toppy_keyboard.enums.KeyboardView;
import com.example.sloppy_toppy_keyboard.keyboardViews.CharactersKeyboardView;
import com.example.sloppy_toppy_keyboard.keyboardViews.MainKeyboardView;

public class CircleKeyboardApplication extends InputMethodService {

    private static final String TAG = "CircleKeyboardApp";

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


    // No modifiers, go in direction
    // Ctrl modifier, move entire word
    // Highlight is enabled, highlight
    // tld: check for modifiers, move in direction, highlight if needed
    // -1 left, 1 right
    public void moveCursor(int direction, boolean ctrlHeld, boolean highlightEnabled) {
        Log.d(TAG, "moveCursor. Direction: " + direction);
        Log.d(TAG, "moveCursor. ctrlHeld: " + ctrlHeld);
        Log.d(TAG, "moveCursor. highlightEnabled: " + highlightEnabled);
        Log.d(TAG, "\n");

        int cursorPosition = getCursorPosition();
        if (!ctrlHeld) {
            inputConnection.setSelection(cursorPosition + direction, cursorPosition + direction);
        }
        else {
            int index = getLeftWordCursorIndex(true);
            // get left word cursor index
             inputConnection.setSelection(index, index);
        }

    }

    private int getLeftWordCursorIndex(boolean beginningOfWord) {
        CharSequence textLeftOfCursor = inputConnection.getTextBeforeCursor(getCursorPosition(), 0);
        Log.d(TAG, "textLeftOfCursor: " + textLeftOfCursor);

        // loop backwards, find first space, index is char before that
        for (int i = textLeftOfCursor.length() - 1; i >= 0; i--) {
            if (i + 1 < textLeftOfCursor.length() && textLeftOfCursor.charAt(i) == ' ') {
                Log.d(TAG, "INDEX: " + i);
                return i + 1;
            }
            else if (i == 0) {
                return 0;
            }
        }
        Log.d(TAG, "INDEX: " + "-1");
        return -1;
    }

//    private int getRightWordCursorIndex(boolean beginningOfWord) {
//
//    }

    // < < > >
    // ctrl, toggle-highlight

    // Arrow goes left/right
    // Ctrl + Arrow goes left word / right word



    // < < < > > >
    // bottom left: ctrl, shift

    // left, right
    // left word, right word
    // left beginning, right end

    // highlight left, delete right
    // highlight left word, delete right word
    // highlight beginning, delete end

    // (I think we should just allow highlighting, then pressing delete for this one)
    // delete left, delete right
    // delete left word, delete right word
    // delete beginning, delete end

    public void highlight() {
        int cursorPosition = getCursorPosition();
        if (cursorPosition - 4 > 0) {
            inputConnection.setSelection(cursorPosition - 4, cursorPosition);
        }
    }

    private int getCursorPosition() {
        ExtractedText extractedText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        if (extractedText != null && extractedText.selectionStart >= 0) {
            return extractedText.selectionStart;
        }
        return -1;
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
