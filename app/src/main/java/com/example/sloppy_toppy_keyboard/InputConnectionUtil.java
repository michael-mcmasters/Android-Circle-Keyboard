package com.example.sloppy_toppy_keyboard;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

public class InputConnectionUtil {

    private CircleKeyboardApplication app;


    public InputConnectionUtil(CircleKeyboardApplication app) {
        this.app = app;
    }

    public int getCursorPosition() {
        ExtractedText extractedText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0);
        if (extractedText != null && extractedText.selectionStart >= 0) {
            return extractedText.selectionStart;
        }
        return -1;
    }

    // Loops backwards from the final cursor position, finds the first space, and returns the index before that
    public int getCtrlLeftCursorPosition() {
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
    public int getCtrlRightCursorPosition() {
        int cursorPosition = getCursorPosition();
        CharSequence allText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text;

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
    public CharSequence getTextLeftOfFinalCursor() {
        CharSequence allText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text;
        StringBuilder leftText = new StringBuilder();
        for (int i = 0; i < getCursorPosition(); i++) {     // getCursorPosition returns final cursor's position
            leftText.append(allText.charAt(i));
        }
        return leftText;
    }

    public int getInputtedTextSize() {
        return app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text.length();
    }

    public boolean textIsHighlighted() {
        ExtractedText allText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0);
        return allText.selectionStart != allText.selectionEnd;
    }

    public boolean isNewSentence() {
        int cursorPosition = getCursorPosition();
        CharSequence charSequence = app.getInputConnection().getTextBeforeCursor(cursorPosition, cursorPosition);

        // If there is no text, this is the beginning of a sentence
        if (charSequence == null || charSequence.length() == 0) {
            return true;
        }

        // Loop from cursor -> backwards. If find period, is a new sentence. If find letter, is not a new sentence.
        for (int i = cursorPosition - 1; i >= 0; i--) {
            if (charSequence.charAt(i) == '.') {
                return true;
            }
            else if (Character.isLetter(charSequence.charAt(i))) {
                return false;
            }
        }

        return false;
    }

    public void sendDownAndUpKeyEvent(int keyEventCode, int flags) {
        sendDownKeyEvent(keyEventCode, flags);
        sendUpKeyEvent(keyEventCode, flags);
    }

    public void sendDownKeyEvent(int keyEventCode, int flags) {
        app.getInputConnection().sendKeyEvent(
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
        app.getInputConnection().sendKeyEvent(
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
