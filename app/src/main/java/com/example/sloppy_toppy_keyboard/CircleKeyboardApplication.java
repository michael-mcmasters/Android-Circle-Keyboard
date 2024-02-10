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
    private InputConnectionUtil inputConnectionUtil;

    private InputConnection inputConnection;
    private ShiftState shiftState;


    @Override
    public View onCreateInputView() {
        this.mainKeyboardView = new MainKeyboardView(this, this);
        this.charactersKeyboardView = new CharactersKeyboardView(this, this);
        this.inputConnectionUtil = new InputConnectionUtil(inputConnection);
        inputConnection.commitText("fdjksl fjdks iowe xnm", 0);
        return mainKeyboardView;
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

    public void commitText(String s) {
        if (s.length() == 1 && Character.isLetter(s.charAt(0))) {
            s = String.valueOf((shiftState == ShiftState.UPPERCASE_ONCE || shiftState == ShiftState.UPPERCASE_ALWAYS)
                    ? Character.toUpperCase(s.charAt(0))
                    : Character.toLowerCase(s.charAt(0)));
        }
        inputConnection.commitText(s, 1);
        setShiftFromCursorPosition();
    }

    public void enter() {
        inputConnectionUtil.sendDownAndUpKeyEvent(KeyEvent.KEYCODE_ENTER, 0);
    }

    public void backspace() {
        CharSequence sel = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(sel)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 0);
        }
        setShiftFromCursorPosition();
    }

    /**
     * Determines if shift should be enabled based on where the cursor is in the text. (e.g. capitalize when at the beginning of a sentence.)
     * Should be called when inputting a char, deleting a char, or moving the cursor
     */
    private void setShiftFromCursorPosition() {
        // If shift is set to always capitalizing letters, then no need to do anything. Just keep capitalizing them.
        if (shiftState == ShiftState.UPPERCASE_ALWAYS) {
            return;
        }

        if (inputConnectionUtil.isNewSentence()) {
            shiftState = ShiftState.UPPERCASE_ONCE;
            mainKeyboardView.capitalizeLettersOnKeyboard(true);
        } else {
            shiftState = ShiftState.LOWERCASE;
            mainKeyboardView.capitalizeLettersOnKeyboard(false);
        }
    }

    /**
     * Called from the shift button
     * @param shiftState
     */
    public void setShiftFromButton(ShiftState shiftState) {
        this.shiftState = shiftState;
        boolean shiftEnabled = shiftState == ShiftState.UPPERCASE_ONCE || shiftState == ShiftState.UPPERCASE_ALWAYS;
        mainKeyboardView.capitalizeLettersOnKeyboard(shiftEnabled);
    }

    // Moves the cursor left/right.
    // If highlightCursorStartPosition has a value, highlights text from its position to the cursor's position.
    public void moveCursorWithArrowButton(KeyboardArrowDirection keyboardArrowDirection, boolean ctrlHeld, int highlightCursorStartPosition) {
        int inputtedTextLength = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length();
        int currentCursorPosition = inputConnectionUtil.getCursorPosition();

        if (ctrlHeld) {
            int newCursorPosition = keyboardArrowDirection == KeyboardArrowDirection.LEFT ? inputConnectionUtil.getCtrlLeftCursorPosition() : inputConnectionUtil.getCtrlRightCursorPosition();
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

    // inputConnection.setSelection always asks for two cursor positions.
    // If highlighting text, you pass both cursor positions
    // If not highlighting text, you pass the first cursor positions twice
    // This method checks if we are highlighting text and sets the value automatically
    private void setCursorPosition(int firstCursorPosition, int highlightCursorStartPosition) {
        int secondCursorPosition = highlightCursorStartPosition == -1 ? firstCursorPosition : highlightCursorStartPosition;
        inputConnection.setSelection(firstCursorPosition, secondCursorPosition);
        setShiftFromCursorPosition();
    }

    public ShiftState getShiftState() {
        return this.shiftState;
    }

    public InputConnectionUtil getInputConnectionUtil() {
        return this.inputConnectionUtil;
    }
}
