package com.example.sloppy_toppy_keyboard.keyboardViews;

import static com.example.sloppy_toppy_keyboard.constants.LongPressActionConstants.CURSOR_END;
import static com.example.sloppy_toppy_keyboard.constants.LongPressActionConstants.CURSOR_HOME;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_LEFT;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_LEFT_WORD;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_RIGHT;
import static com.example.sloppy_toppy_keyboard.constants.TapActionConstants.CURSOR_RIGHT_WORD;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.R;
import com.example.sloppy_toppy_keyboard.enums.KeyboardArrowDirection;
import com.example.sloppy_toppy_keyboard.listeners.ShiftListener;
import com.example.sloppy_toppy_keyboard.model.KeyBindings;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.model.KeyProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
import com.example.sloppy_toppy_keyboard.listeners.EnterListener;
import com.example.sloppy_toppy_keyboard.listeners.NumListener;
import com.example.sloppy_toppy_keyboard.old.CircleOnPressListener;

import java.io.IOException;
import java.io.InputStream;

public class MainKeyboardView extends ConstraintLayout {

    private static final String TAG = "MainKeyboardView";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private CircleOnPressListener leftCircleOnPressListener;
    private CircleOnPressListener rightCircleOnPressListener;

    private String leftCircleState;
    private String rightCircleState;

    private Boolean modButtonHeld;
    private Integer highlightCursorStartPosition;
    private int previousInputtedTextSize;


    public MainKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        super(context);
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        leftCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, true);
        rightCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, false);

        leftCircleState = "ACTION_UP";
        rightCircleState = "ACTION_UP";

        modButtonHeld = false;
        highlightCursorStartPosition = -1;
        previousInputtedTextSize = -1;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.key_layout, this);

        KeyBindings keyBindings = getKeyMapFromConfigFile(R.raw.key_bindings);
        setLettersVisually(keyBindings);
        setLettersFunctionally(keyBindings);
    }

    // Sets what the user sees on the keyboard
    private void setLettersVisually(KeyBindings keyBindings) {
        setButtonsLettersVisually(findViewById(R.id.topLeftButtonLayout), keyBindings.getTopLeft());
        setButtonsLettersVisually(findViewById(R.id.topRightButtonLayout), keyBindings.getTopRight());
        setButtonsLettersVisually(findViewById(R.id.bottomLeftButtonLayout), keyBindings.getBottomLeft());
        setButtonsLettersVisually(findViewById(R.id.bottomRightButtonLayout), keyBindings.getBottomRight());
    }

    // Sets the touch gestures of the keyboard
    private void setLettersFunctionally(KeyBindings keyBindings) {
        findViewById(R.id.topLeftButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, this, keyBindings.getTopLeft()).getButtonCallback()
        );
        findViewById(R.id.topRightButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, this, keyBindings.getTopRight()).getButtonCallback()
        );
        findViewById(R.id.bottomLeftButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, this, keyBindings.getBottomLeft()).getButtonCallback()
        );
        findViewById(R.id.bottomRightButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, this, keyBindings.getBottomRight()).getButtonCallback()
        );
        findViewById(R.id.backspaceButton).setOnTouchListener(
            new BackspaceListener(context, circleKeyboardApplication).getButtonCallback()
        );
        findViewById(R.id.shiftButton).setOnTouchListener(
            new ShiftListener(context, circleKeyboardApplication, this).getButtonCallback()
        );
        findViewById(R.id.numButton).setOnTouchListener(
            new NumListener(context, circleKeyboardApplication).getButtonCallback()
        );
        findViewById(R.id.spaceButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                modButtonHeld = true;
                previousInputtedTextSize = circleKeyboardApplication.getInputConnectionUtil().getInputtedTextSize();
                highlightCursorStartPosition = circleKeyboardApplication.getInputConnectionUtil().getCursorPosition();
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                boolean performendModAction = highlightCursorStartPosition != circleKeyboardApplication.getInputConnectionUtil().getCursorPosition()
                        || circleKeyboardApplication.getInputConnectionUtil().getInputtedTextSize() != previousInputtedTextSize
                        || circleKeyboardApplication.getInputConnectionUtil().textIsHighlighted();

                if (!performendModAction) {
                    circleKeyboardApplication.write(" ");
                }

                modButtonHeld = false;
                highlightCursorStartPosition = -1;
            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });
        findViewById(R.id.enterButton).setOnTouchListener(
            new EnterListener(context, circleKeyboardApplication).getButtonCallback()
        );
    }

    private void setButtonsLettersVisually(ViewGroup viewGroup, KeyMap keyMap) {
        int propertyIndex = 0;  // used to switch up, left, down, right, farUp, farLeft, farDown, farRight

        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            View child = viewGroup.getChildAt(index);
            if (child instanceof TextView && ((TextView) child).getText() != "") {
                KeyProperties keyPropertes = keyMap.getPropertyByIndex(keyMap, propertyIndex);
                String character = keyPropertes.getVisual();
                ((TextView) child).setText(character);
                propertyIndex++;
            }
        }
    }

    public void performLongpressAction(String longPressAction) {
        if (longPressAction.equals(CURSOR_HOME)) {
            circleKeyboardApplication.moveCursorViaHomeButton(highlightCursorStartPosition);
        } else if (longPressAction.equals(CURSOR_END)) {
            circleKeyboardApplication.moveCursorViaEndButton(highlightCursorStartPosition);
        }

    }

    public void performTapAction(String tapAction) {
        if (tapAction.equals(CURSOR_LEFT)) {
            circleKeyboardApplication.moveCursorViaArrowButton(KeyboardArrowDirection.LEFT, false, highlightCursorStartPosition);
        } else if (tapAction.equals(CURSOR_RIGHT)) {
            circleKeyboardApplication.moveCursorViaArrowButton(KeyboardArrowDirection.RIGHT, false, highlightCursorStartPosition);
        } else if (tapAction.equals(CURSOR_LEFT_WORD)) {
            circleKeyboardApplication.moveCursorViaArrowButton(KeyboardArrowDirection.LEFT, true, highlightCursorStartPosition);
        } else if (tapAction.equals(CURSOR_RIGHT_WORD)) {
            circleKeyboardApplication.moveCursorViaArrowButton(KeyboardArrowDirection.RIGHT, true, highlightCursorStartPosition);
        }
    }

    /**
     * Sets the appearance of letters on the keyboard. If swiping on them actually inputs a capital letter or not is determined by another class.
     * @param upperCase
     */
    public void capitalizeLettersOnKeyboard(boolean upperCase) {
        setUpperCase(findViewById(R.id.key1), upperCase);
        setUpperCase(findViewById(R.id.key2), upperCase);
        setUpperCase(findViewById(R.id.key3), upperCase);
        setUpperCase(findViewById(R.id.key4), upperCase);
        setUpperCase(findViewById(R.id.key5), upperCase);
        setUpperCase(findViewById(R.id.key6), upperCase);
        setUpperCase(findViewById(R.id.key7), upperCase);
        setUpperCase(findViewById(R.id.key8), upperCase);
        setUpperCase(findViewById(R.id.key9), upperCase);
        setUpperCase(findViewById(R.id.key10), upperCase);
        setUpperCase(findViewById(R.id.key11), upperCase);
        setUpperCase(findViewById(R.id.key12), upperCase);
        setUpperCase(findViewById(R.id.key13), upperCase);
        setUpperCase(findViewById(R.id.key14), upperCase);
        setUpperCase(findViewById(R.id.key15), upperCase);
        setUpperCase(findViewById(R.id.key16), upperCase);
        setUpperCase(findViewById(R.id.key17), upperCase);
        setUpperCase(findViewById(R.id.key18), upperCase);
        setUpperCase(findViewById(R.id.key19), upperCase);
        setUpperCase(findViewById(R.id.key20), upperCase);
        setUpperCase(findViewById(R.id.key21), upperCase);
        setUpperCase(findViewById(R.id.key22), upperCase);
        setUpperCase(findViewById(R.id.key23), upperCase);
        setUpperCase(findViewById(R.id.key24), upperCase);
        setUpperCase(findViewById(R.id.key25), upperCase);
        setUpperCase(findViewById(R.id.key26), upperCase);
        setUpperCase(findViewById(R.id.key27), upperCase);
        setUpperCase(findViewById(R.id.key28), upperCase);
        setUpperCase(findViewById(R.id.key29), upperCase);
        setUpperCase(findViewById(R.id.key30), upperCase);
        setUpperCase(findViewById(R.id.key31), upperCase);
        setUpperCase(findViewById(R.id.key32), upperCase);
    }

    private void setUpperCase(View view, boolean upperCase) {
        try {
            if (!(view instanceof TextView)) return;

            TextView textView = (TextView) view;
            boolean isNotLetter = textView.getText() == null || textView.getText().equals("") || textView.getText().length() > 1;
            if (isNotLetter) {
                return;
            }

            char c = textView.getText().charAt(0);
            if (Character.isLetter(c)) {
                char cc = upperCase ? Character.toUpperCase(c) : Character.toLowerCase(c);
                textView.setText(Character.toString(cc));
            }
        } catch (Exception e) {
            Log.e(TAG, "toggleUpperCase: Exception capitalizing letter");
        }
    }

    // This method maybe shouldn't be in this View class. Should create another class that instantiates this view class and the CircleOnPressListener class?
    public void notifyButtonState(boolean leftCircle, String circleState) {
//        if (leftCircle) {
//            leftCircleState = circleState;
//        } else {
//            rightCircleState = circleState;
//        }
//
//        if (leftCircleState.equals("ACTION_UP") && rightCircleState.equals("ACTION_UP")) {
//            circleKeyboardApplication.commitText(" ");
//        }
    }

    private KeyBindings getKeyMapFromConfigFile(int fileName) {
        try {
            return parseConfigFile(fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading file");
            throw new RuntimeException("Can not parse file");
        }
    }

    private KeyBindings parseConfigFile(int fileName) throws IOException {
        InputStream inputStream = null;
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            inputStream = getResources().openRawResource(fileName);
            return mapper.readValue(inputStream, KeyBindings.class);
        } catch (IOException e) {
            Log.e(TAG, "Error reading file");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        throw new RuntimeException("Can not parse file");
    }
}
