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
import com.example.sloppy_toppy_keyboard.enums.ShiftState;
import com.example.sloppy_toppy_keyboard.keyboardViews.CharactersKeyboardView;
import com.example.sloppy_toppy_keyboard.keyboardViews.MainKeyboardView;

public class CircleKeyboardApplication extends InputMethodService {

    private static final String TAG = "CircleKeyboardApp";

    private MainKeyboardView mainKeyboardView;
    private CharactersKeyboardView charactersKeyboardView;

    private InputConnection inputConnection;
//    private boolean shiftEnabled;
    private ShiftState shiftState;


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

    // Moves the cursor left/right.
    // If highlightCursorStartPosition has a value, highlights text from its position to the cursor's position.
    public void moveCursorWithArrowButton(KeyboardArrowDirection keyboardArrowDirection, boolean ctrlHeld, int highlightCursorStartPosition) {
        int inputtedTextLength = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length();
        int currentCursorPosition = getCursorPosition();

        if (ctrlHeld) {
            int newCursorPosition = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? getCtrlLeftCursorPosition() : getCtrlRightCursorPosition();
            setCursorPosition(newCursorPosition, highlightCursorStartPosition);
        }
        else {
            int direction = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? -1 : 1;
            int newCursorPosition = currentCursorPosition + direction;
            if (newCursorPosition < 0 || newCursorPosition > inputtedTextLength) {
                newCursorPosition = currentCursorPosition;
            }
            setCursorPosition(newCursorPosition, highlightCursorStartPosition);
        }
    }

    public void moveCursorWithHomeButton(int highlightCursorStartPosition) {
        int newCursorPosition = 0;
        setCursorPosition(newCursorPosition, highlightCursorStartPosition);
    }

    public void moveCursorWithEndButton(int highlightCursorStartPosition) {
        int newCursorPosition = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length();
        setCursorPosition(newCursorPosition, highlightCursorStartPosition);
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

    // Not 100% what all code here does but it works. Probably possibly can delete some of it as it was copied from elsewhere.
    public void backspace() {
        CharSequence sel = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(sel)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 0);
        }
        updateShiftState();
    }

    public void enter() {
        sendDownAndUpKeyEvent(KeyEvent.KEYCODE_ENTER, 0);
    }

    public void shift(boolean enabled) {
        Log.d("", "Parent Shift");
//        shiftEnabled = enabled;

        if (enabled) {
            shiftState = ShiftState.LOWERCASE;
        }

    }

    public int getInputtedTextSize() {
        return inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length();
    }

    public boolean textIsHighlighted() {
        ExtractedText allText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
        return allText.selectionStart != allText.selectionEnd;
    }

    private void sendDownAndUpKeyEvent(int keyEventCode, int flags) {
        sendDownKeyEvent(keyEventCode, flags);
        sendUpKeyEvent(keyEventCode, flags);
    }

    // inputConnection.setSelection always asks for two cursor positions.
    // If highlighting text, you pass both cursor positions
    // If not highlighting text, you pass the first cursor positions twice
    // This method checks if we are highlighting text and sets the value automatically
    private void setCursorPosition(int firstCursorPosition, int highlightCursorStartPosition) {
        int secondCursorPosition = highlightCursorStartPosition == -1 ? firstCursorPosition : highlightCursorStartPosition;
        inputConnection.setSelection(firstCursorPosition, secondCursorPosition);
        updateShiftState();
    }


//    public void commitText(String s) {
//        // * lower *
//        // - if beg of text: uppercase
//        // - if not: lowercase
//
//        // * upper_once *
//        // - if beg of text: uppercase, then set var to lowercase
//        // - if not, uppercase, then set var to lowercase
//
//        // * upper_always *
//        // - if beg of text, uppercase
//        // - if not, uppercase
//
//        // TODO: Test this, then set shiftState in shift button listener
//        // Also mainKeyboardView.shift isn't changing letters visually. Not sure what I did wrong.
//
//        int cursorPosition = getCursorPosition();
//        boolean beginningOfSentence = inputConnection.getTextBeforeCursor(cursorPosition, cursorPosition).length() == 0;    // TODO: Need to check if period appears before cursor. Not just check if at position 0.
//
//        if (shiftState == ShiftState.LOWERCASE) {
//            if (beginningOfSentence) {
//                s = String.valueOf(Character.toUpperCase(s.charAt(0)));
//                mainKeyboardView.shift(true);
//            } else {
//                s = String.valueOf(Character.toLowerCase(s.charAt(0)));
//                mainKeyboardView.shift(false);
//            }
//        }
//        else if (shiftState == ShiftState.UPPERCASE_ONCE) {
//            if (beginningOfSentence) {
//                s = String.valueOf(Character.toUpperCase(s.charAt(0)));
//                mainKeyboardView.shift(true);
//            } else {
//                s = String.valueOf(Character.toUpperCase(s.charAt(0)));
//                mainKeyboardView.shift(true);
//            }
//            shiftState = ShiftState.LOWERCASE;
//        }
//        else if (shiftState == ShiftState.UPPERCASE_ALWAYS) {
//            s = String.valueOf(Character.toUpperCase(s.charAt(0)));
//            mainKeyboardView.shift(true);
//        }
//
//        setShiftState();
//
//
////        int cursorPosition = getCursorPosition();
////        int len = inputConnection.getTextBeforeCursor(cursorPosition, cursorPosition).length();
//
////        if (s.length() == 1 && Character.isLetter(s.charAt(0))) {
////            s = String.valueOf(shiftEnabled ? Character.toUpperCase(s.charAt(0)) : Character.toLowerCase(s.charAt(0)));
////        }
//        inputConnection.commitText(s, 1);
//    }


    public void commitText(String s) {
        if (s.length() == 1 && Character.isLetter(s.charAt(0))) {
            s = String.valueOf((shiftState == ShiftState.UPPERCASE_ONCE || shiftState == ShiftState.UPPERCASE_ALWAYS)
                    ? Character.toUpperCase(s.charAt(0))
                    : Character.toLowerCase(s.charAt(0)));
        }
        inputConnection.commitText(s, 1);
        updateShiftState();
    }

    // Call this when
    // move cursor
    // every time type letter
    // every time backspace
    private void updateShiftState() {
        // beg of sentence, enable
        // !beg of sentence, disable
        // set to UPPERCASE_ALWAYS, enable

        if (shiftState == ShiftState.UPPERCASE_ALWAYS) {
            mainKeyboardView.shift(true);
            return;
        }

        int cursorPosition = getCursorPosition();
        boolean beginningOfSentence = inputConnection.getTextBeforeCursor(cursorPosition, cursorPosition).length() == 0;    // TODO: Need to check if period appears before cursor. Not just check if at position 0.

        if (beginningOfSentence) {
            shiftState = ShiftState.UPPERCASE_ONCE;
            mainKeyboardView.shift(true);
        } else {
            shiftState = ShiftState.LOWERCASE;
            mainKeyboardView.shift(false);
        }
    }

    public void toggleShift(boolean uppercaseAlways) {
        if (uppercaseAlways) {
            shiftState = ShiftState.UPPERCASE_ALWAYS;
            mainKeyboardView.shift(true);
            return;
        }

        switch (shiftState) {
            case UPPERCASE_ALWAYS:
                shiftState = ShiftState.LOWERCASE;
                mainKeyboardView.shift(false);
                break;
            case UPPERCASE_ONCE:
                shiftState = ShiftState.LOWERCASE;
                mainKeyboardView.shift(false);
                break;
            case LOWERCASE:
                shiftState = ShiftState.UPPERCASE_ONCE;
                mainKeyboardView.shift(true);
                break;
        }
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
