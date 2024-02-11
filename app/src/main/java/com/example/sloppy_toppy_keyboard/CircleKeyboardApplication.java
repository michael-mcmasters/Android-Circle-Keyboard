package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

    /**
     * *** Method lifecycle ***
     *
     * First time opening keyboard
     * 1. onInitializeInterface
     * 2. onBindInput
     * 3. onStartInput
     * 4. onCreateInputView
     *
     * Collapsing keyboard
     * 1. onBindInput
     * 2. onStartInput
     *
     * Opening keyboard again
     * 1. onBindInput
     * 2. onStartInput
     */

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        inputConnection = getCurrentInputConnection();
    }

    @Override
    public void onBindInput() {
        inputConnection = getCurrentInputConnection();

        // If null, it's the first time keyboard is opened. If not null, keyboard was collapsed and re-opened so need to reset values
        if (mainKeyboardView != null) {
            resetValues();
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        inputConnection = getCurrentInputConnection();
    }

    // Set initial values here
    @Override
    public View onCreateInputView() {
        this.mainKeyboardView = new MainKeyboardView(this, this);
        this.charactersKeyboardView = new CharactersKeyboardView(this, this);
        this.inputConnectionUtil = new InputConnectionUtil(this);
        resetValues();
//        inputConnection.commitText("fdjksl fjdks iowe xnm", 0);
        return mainKeyboardView;
    }

    private void resetValues() {
        toggleShiftViaCursorPosition();
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

    public void write(String s) {
        if (s.length() == 1 && Character.isLetter(s.charAt(0))) {
            s = String.valueOf((shiftState == ShiftState.UPPERCASE_ONCE || shiftState == ShiftState.UPPERCASE_ALWAYS)
                    ? Character.toUpperCase(s.charAt(0))
                    : Character.toLowerCase(s.charAt(0)));
        }
        inputConnection.commitText(s, 1);
        toggleShiftViaCursorPosition();
    }

    public void backspace() {
        CharSequence sel = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(sel)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 0);
        }
        toggleShiftViaCursorPosition();
    }

    public void enter() {
        inputConnectionUtil.sendDownAndUpKeyEvent(KeyEvent.KEYCODE_ENTER, 0);
    }

    // Moves the cursor left/right.
    // If highlightCursorStartPosition has a value, highlights text from its position to the cursor's position.
    public void moveCursorViaArrowButton(KeyboardArrowDirection keyboardArrowDirection, boolean ctrlHeld, int highlightCursorStartPosition) {
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

    public void moveCursorViaHomeButton(int highlightCursorStartPosition) {
        int newCursorPosition = 0;
        setCursorPosition(newCursorPosition, highlightCursorStartPosition);
    }

    public void moveCursorViaEndButton(int highlightCursorStartPosition) {
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
        toggleShiftViaCursorPosition();
    }

    /**
     * Called from the shift button
     * @param shiftState
     */
    public void toggleShiftViaButton(ShiftState shiftState) {
        this.shiftState = shiftState;
        boolean shiftEnabled = shiftState == ShiftState.UPPERCASE_ONCE || shiftState == ShiftState.UPPERCASE_ALWAYS;
        mainKeyboardView.capitalizeLettersOnKeyboard(shiftEnabled);
    }

    /**
     * Determines if shift should be enabled based on where the cursor is in the text. (e.g. capitalize when at the beginning of a sentence.)
     * Should be called when inputting a char, deleting a char, or moving the cursor
     */
    private void toggleShiftViaCursorPosition() {
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


    public InputConnection getInputConnection() {
        return this.inputConnection;
    }

    public InputConnectionUtil getInputConnectionUtil() {
        return this.inputConnectionUtil;
    }

    public ShiftState getShiftState() {
        return this.shiftState;
    }

}
