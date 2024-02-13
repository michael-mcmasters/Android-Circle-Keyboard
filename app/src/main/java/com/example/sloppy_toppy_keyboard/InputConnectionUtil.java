package com.example.sloppy_toppy_keyboard;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

public class InputConnectionUtil {

    private CircleKeyboardApplication app;


    public InputConnectionUtil(CircleKeyboardApplication app) {
        this.app = app;
    }

    // Note: If you are highlighting text, this may return the position of the right-most cursor
    public int getCursorPosition() {
        ExtractedText extractedText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0);
        if (extractedText != null && extractedText.selectionStart >= 0) {
            return extractedText.selectionStart;
        }
        return -1;
    }

    // Loops backwards from the final cursor position, finds the first space, and returns the index before that
//    public int getCtrlLeftCursorPosition() {
//        CharSequence textLeftOfCursor = getTextLeftOfFinalCursor();
//        for (int i = textLeftOfCursor.length() - 1; i >= 0; i--) {
//            if (i + 1 < textLeftOfCursor.length() && textLeftOfCursor.charAt(i) == ' ') {
//                return i + 1;
//            } else if (i == 0) {
//                return 0;
//            }
//        }
//        return -1;
//    }

    // Loops forwards starting at the cursor position, finds first space to identify the end of the current word, finds second non-space character to returns that index (to put cursor before it)
    public int getCtrlRightCursorPosition() {
        int cursorPosition = getCursorPosition();
        CharSequence allText = app.getInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text;

        boolean foundEndOfCurrentWord = false;
        for (int i = cursorPosition; i < allText.length(); i++) {
            char currentLetter = allText.charAt(i);

            if (!foundEndOfCurrentWord && currentLetter == ' ') {
                foundEndOfCurrentWord = true;
            } else if (foundEndOfCurrentWord && currentLetter != ' ') {
                return i;
            } else if (i == allText.length() - 1) {
                return allText.length();
            }
        }
        return -1;
    }

    // Loops backwards from the right-most cursor position, finds first space to identify the beginning of the current word, finds second non-space character to identify the end of the next word
    public int getCtrlLeftCursorPosition() {
        CharSequence textLeftOfCursor = getTextLeftOfFinalCursor();

        boolean foundBeginningOfCurrentWord = false;
        for (int i = textLeftOfCursor.length() - 1; i >= 0; i--) {
            if (i <= 0) return 0;

            char currentLetter = textLeftOfCursor.charAt(i);

            if (!foundBeginningOfCurrentWord && currentLetter == ' ') {
                foundBeginningOfCurrentWord = true;
            } else if (foundBeginningOfCurrentWord && currentLetter != ' ') {
                return i + 1;
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
        for (int i = charSequence.length() - 1; i >= 0; i--) {
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
