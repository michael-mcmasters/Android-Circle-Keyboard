package com.example.sloppy_toppy_keyboard.keyboardViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.R;
import com.example.sloppy_toppy_keyboard.listeners.ShiftListener;
import com.example.sloppy_toppy_keyboard.model.KeyBindingsYaml;
import com.example.sloppy_toppy_keyboard.model.ButtonKeyBindings;
import com.example.sloppy_toppy_keyboard.model.Key;
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
    private CircleKeyboardApplication keyboardApp;
    private CircleOnPressListener leftCircleOnPressListener;
    private CircleOnPressListener rightCircleOnPressListener;

    private String leftCircleState;
    private String rightCircleState;

    private Boolean modButtonHeld;
//    private Integer highlightCursorStartPosition;
    private int previousInputtedTextSize;


    public MainKeyboardView(Context context, CircleKeyboardApplication keyboardApp) {
        super(context);
        this.context = context;
        this.keyboardApp = keyboardApp;
        leftCircleOnPressListener = new CircleOnPressListener(context, keyboardApp, this, true);
        rightCircleOnPressListener = new CircleOnPressListener(context, keyboardApp, this, false);

        leftCircleState = "ACTION_UP";
        rightCircleState = "ACTION_UP";

        modButtonHeld = false;
//        highlightCursorStartPosition = -1;
        previousInputtedTextSize = -1;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.key_layout, this);

        KeyBindingsYaml keyBindingsYaml = getKeyMapFromConfigFile(R.raw.key_bindings);
        setLettersVisually(keyBindingsYaml);
        setLettersFunctionally(keyBindingsYaml);
    }

    // Sets what the user sees on the keyboard
    private void setLettersVisually(KeyBindingsYaml keyBindingsYaml) {
        setButtonsLettersVisually(findViewById(R.id.topLeftButtonLayout), keyBindingsYaml.getTopLeftButtonKeyMap());
        setButtonsLettersVisually(findViewById(R.id.topRightButtonLayout), keyBindingsYaml.getTopRightButtonKeyMap());
        setButtonsLettersVisually(findViewById(R.id.bottomLeftButtonLayout), keyBindingsYaml.getBottomLeftButtonKeyMap());
        setButtonsLettersVisually(findViewById(R.id.bottomRightButtonLayout), keyBindingsYaml.getBottomRightButtonKeyMap());
    }

    // Sets the touch gestures of the keyboard
    private void setLettersFunctionally(KeyBindingsYaml keyBindingsYaml) {
        findViewById(R.id.topLeftButton).setOnTouchListener(
            new ButtonListener(context, keyboardApp, this, keyBindingsYaml.getTopLeftButtonKeyMap()).getButtonCallback()
        );
        findViewById(R.id.topRightButton).setOnTouchListener(
            new ButtonListener(context, keyboardApp, this, keyBindingsYaml.getTopRightButtonKeyMap()).getButtonCallback()
        );
        findViewById(R.id.bottomLeftButton).setOnTouchListener(
            new ButtonListener(context, keyboardApp, this, keyBindingsYaml.getBottomLeftButtonKeyMap()).getButtonCallback()
        );
        findViewById(R.id.bottomRightButton).setOnTouchListener(
            new ButtonListener(context, keyboardApp, this, keyBindingsYaml.getBottomRightButtonKeyMap()).getButtonCallback()
        );
        findViewById(R.id.backspaceButton).setOnTouchListener(
            new BackspaceListener(context, keyboardApp).getButtonCallback()
        );
        findViewById(R.id.shiftButton).setOnTouchListener(
            new ShiftListener(context, keyboardApp, this).getButtonCallback()
        );
        findViewById(R.id.numButton).setOnTouchListener(
            new NumListener(context, keyboardApp).getButtonCallback()
        );
        findViewById(R.id.spaceButton).setOnTouchListener((view, motionEvent) -> {
            Integer fingerAction = MotionEventCompat.getActionMasked(motionEvent);
            if (fingerAction.equals(MotionEvent.ACTION_DOWN)) {
                modButtonHeld = true;
                previousInputtedTextSize = keyboardApp.getInputConnectionUtil().getInputtedTextSize();
                keyboardApp.setHighlightCursorStartPosition(keyboardApp.getInputConnectionUtil().getCursorPosition());
            } else if (fingerAction.equals(MotionEvent.ACTION_UP)) {
                boolean userPerformedModActionWhileHoldingSpace = keyboardApp.getHighlightCursorStartPosition() != keyboardApp.getInputConnectionUtil().getCursorPosition()
                        || keyboardApp.getInputConnectionUtil().getInputtedTextSize() != previousInputtedTextSize
                        || keyboardApp.getInputConnectionUtil().textIsHighlighted();

                if (!userPerformedModActionWhileHoldingSpace) {
                    keyboardApp.write(keyBindingsYaml.getBaseKeyMap().getSpace());
                }

                modButtonHeld = false;
                keyboardApp.setHighlightCursorStartPosition(-1);
            }

            view.performClick();    // intellij gets mad if I don't add this. Not sure what it does
            return true;
        });
        findViewById(R.id.enterButton).setOnTouchListener(
            new EnterListener(context, keyboardApp).getButtonCallback()
        );
    }

    private void setButtonsLettersVisually(ViewGroup viewGroup, ButtonKeyBindings buttonKeyBindings) {
        int propertyIndex = 0;  // used to switch up, left, down, right, farUp, farLeft, farDown, farRight

        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            View child = viewGroup.getChildAt(index);
            if (child instanceof TextView && ((TextView) child).getText() != "") {
                Key keyPropertes = buttonKeyBindings.getPropertyByIndex(propertyIndex);
                String character = keyPropertes.getVisual();
                ((TextView) child).setText(character);
                propertyIndex++;
            }
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

    private KeyBindingsYaml getKeyMapFromConfigFile(int fileName) {
        try {
            return parseConfigFile(fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading file");
            throw new RuntimeException("Can not parse file");
        }
    }

    private KeyBindingsYaml parseConfigFile(int fileName) throws IOException {
        InputStream inputStream = null;
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            inputStream = getResources().openRawResource(fileName);
            return mapper.readValue(inputStream, KeyBindingsYaml.class);
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
