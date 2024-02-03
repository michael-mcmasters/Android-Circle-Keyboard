package com.example.sloppy_toppy_keyboard.keyboardViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sloppy_toppy_keyboard.CircleKeyboardApplication;
import com.example.sloppy_toppy_keyboard.R;
import com.example.sloppy_toppy_keyboard.listeners.ShiftListener;
import com.example.sloppy_toppy_keyboard.model.KeyBindings;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
import com.example.sloppy_toppy_keyboard.listeners.EnterListener;
import com.example.sloppy_toppy_keyboard.listeners.NumListener;
import com.example.sloppy_toppy_keyboard.listeners.SpaceListener;
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

    public MainKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        super(context);
        this.context = context;
        this.circleKeyboardApplication = circleKeyboardApplication;
        leftCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, true);
        rightCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, this, false);

        leftCircleState = "ACTION_UP";
        rightCircleState = "ACTION_UP";

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
            new ButtonListener(context, circleKeyboardApplication, keyBindings.getTopLeft()).getButtonCallback()
        );
        findViewById(R.id.topRightButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, keyBindings.getTopRight()).getButtonCallback()
        );
        findViewById(R.id.bottomLeftButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, keyBindings.getBottomLeft()).getButtonCallback()
        );
        findViewById(R.id.bottomRightButton).setOnTouchListener(
            new ButtonListener(context, circleKeyboardApplication, keyBindings.getBottomRight()).getButtonCallback()
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
        findViewById(R.id.spaceButton).setOnTouchListener(
            new SpaceListener(context, circleKeyboardApplication).getButtonCallback()
        );
        findViewById(R.id.enterButton).setOnTouchListener(
            new EnterListener(context, circleKeyboardApplication).getButtonCallback()
        );
    }

    private void setButtonsLettersVisually(ViewGroup viewGroup, KeyMap keyMap) {
        int propertyIndex = 0;  // used to switch up, left, down, right, farUp, farLeft, farDown, farRight

        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            View child = viewGroup.getChildAt(index);
            Log.d(TAG, "setLettersVisually: " + child);
            if (child instanceof TextView && ((TextView) child).getText() != "") {
                String character = keyMap.getPropertyValueByIndex(keyMap, propertyIndex);
                ((TextView) child).setText(character);
                propertyIndex++;
            }
        }
    }

    public void shift(boolean upperCase) {
        Log.d("", "Circle Shift");

        // Capitalize actual letters
        circleKeyboardApplication.shift(upperCase);

        // Capitalize visual letters
        toggleUpperCase(findViewById(R.id.key1), upperCase);
        toggleUpperCase(findViewById(R.id.key2), upperCase);
        toggleUpperCase(findViewById(R.id.key3), upperCase);
        toggleUpperCase(findViewById(R.id.key4), upperCase);
        toggleUpperCase(findViewById(R.id.key5), upperCase);
        toggleUpperCase(findViewById(R.id.key6), upperCase);
        toggleUpperCase(findViewById(R.id.key7), upperCase);
        toggleUpperCase(findViewById(R.id.key8), upperCase);
        toggleUpperCase(findViewById(R.id.key9), upperCase);
        toggleUpperCase(findViewById(R.id.key10), upperCase);
        toggleUpperCase(findViewById(R.id.key11), upperCase);
        toggleUpperCase(findViewById(R.id.key12), upperCase);
        toggleUpperCase(findViewById(R.id.key13), upperCase);
        toggleUpperCase(findViewById(R.id.key14), upperCase);
        toggleUpperCase(findViewById(R.id.key15), upperCase);
        toggleUpperCase(findViewById(R.id.key16), upperCase);
        toggleUpperCase(findViewById(R.id.key17), upperCase);
        toggleUpperCase(findViewById(R.id.key18), upperCase);
        toggleUpperCase(findViewById(R.id.key19), upperCase);
        toggleUpperCase(findViewById(R.id.key20), upperCase);
        toggleUpperCase(findViewById(R.id.key21), upperCase);
        toggleUpperCase(findViewById(R.id.key22), upperCase);
        toggleUpperCase(findViewById(R.id.key23), upperCase);
        toggleUpperCase(findViewById(R.id.key24), upperCase);
        toggleUpperCase(findViewById(R.id.key25), upperCase);
        toggleUpperCase(findViewById(R.id.key26), upperCase);
        toggleUpperCase(findViewById(R.id.key27), upperCase);
        toggleUpperCase(findViewById(R.id.key28), upperCase);
        toggleUpperCase(findViewById(R.id.key29), upperCase);
        toggleUpperCase(findViewById(R.id.key30), upperCase);
        toggleUpperCase(findViewById(R.id.key31), upperCase);
        toggleUpperCase(findViewById(R.id.key32), upperCase);
    }

    private void toggleUpperCase(View view, boolean upperCase) {
        try {
            if (!(view instanceof TextView)) return;

            TextView textView = (TextView) view;
            if (textView.getText() == null || textView.getText().equals("")) return;

            char c = textView.getText().charAt(0);
            if (Character.isLetter(c)) {
//                char cc = Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);
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
