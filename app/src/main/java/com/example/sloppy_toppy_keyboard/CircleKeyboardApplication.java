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

import com.example.sloppy_toppy_keyboard.enums.KeyboardArrowDirection;
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
        inputConnection.commitText("fdjksl fjdks iowe xnm", 0);
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
    public void moveCursor(KeyboardArrowDirection keyboardArrowDirection, boolean ctrlHeld, int highlightCursorStartPosition) {
        Log.d(TAG, "moveCursor. keyboardArrowDirection: " + keyboardArrowDirection);
        Log.d(TAG, "moveCursor. ctrlHeld: " + ctrlHeld);
        Log.d(TAG, "moveCursor. highlightCursorStartPosition: " + highlightCursorStartPosition);
        Log.d(TAG, "\n");

        int inputtedTextLength = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length();
        boolean highlightButtonHeld = highlightCursorStartPosition != -1;

        if (highlightButtonHeld) {
            int cursorPosition = getCursorPosition();
            if (ctrlHeld) {
                int index = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? getCtrlLeftCursorPosition() : getCtrlRightCursorPosition();
                inputConnection.setSelection(index, highlightCursorStartPosition);
            }
            else {
                int direction = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? -1 : 1;
                int newPosition = cursorPosition + direction;
                if (newPosition < 0 || newPosition > inputtedTextLength) {
                    newPosition = cursorPosition;
                }
                inputConnection.setSelection(newPosition, highlightCursorStartPosition);
            }
        }
        else {
            int cursorPosition = getCursorPosition();
            if (ctrlHeld) {
                int index = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? getCtrlLeftCursorPosition() : getCtrlRightCursorPosition();
                inputConnection.setSelection(index, index);
            }
            else {
                int direction = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? -1 : 1;
                int newPosition = cursorPosition + direction;
                if (newPosition < 0 || newPosition > inputtedTextLength) {
                    newPosition = cursorPosition;
                }
                inputConnection.setSelection(newPosition, newPosition);
            }
        }
    }

    // Loops backwards from the final cursor position, finds the first space, and returns the index before that
    private int getCtrlLeftCursorPosition() {
        CharSequence textLeftOfCursor = getTextLeftOfFinalCursor();
        for (int i = textLeftOfCursor.length() - 1; i >= 0; i--) {
            if (i + 1 < textLeftOfCursor.length() && textLeftOfCursor.charAt(i) == ' ') {
                return i + 1;
            } else if (i == 0) {
                return 0;
            }
        }
        return -1;
    }

    // Loops forwards starting at the cursor position, finds the first space, and returns the index before it
    private int getCtrlRightCursorPosition() {
        int cursorPosition = getCursorPosition();
        CharSequence allText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;

        for (int i = cursorPosition; i < allText.length(); i++) {
            if (i >= 1 && allText.charAt(i) == ' ') {
                return i + 1;
            } else if (i == allText.length() - 1) {
                return allText.length();
            }
        }
        return -1;
    }

    // Returns all text up until the final cursor. (You'll have 2 cursors if highlighting text.)
    // This method is needed because inputConnection.getTextBeforeCursor() only returns text before the *first* cursor.
    private CharSequence getTextLeftOfFinalCursor() {
        CharSequence allText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
        StringBuilder leftText = new StringBuilder();
        for (int i = 0; i < getCursorPosition(); i++) {     // getCursorPosition returns final cursor's position
            leftText.append(allText.charAt(i));
        }
        return leftText;
    }

    public int getCursorPosition() {
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
