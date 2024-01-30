package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sloppy_toppy_keyboard.listeners.ShiftListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
import com.example.sloppy_toppy_keyboard.listeners.EnterListener;
import com.example.sloppy_toppy_keyboard.listeners.NumListener;
import com.example.sloppy_toppy_keyboard.listeners.SpaceListener;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.old.CircleOnPressListener;

import java.io.IOException;
import java.io.InputStream;

public class MainKeyboardView extends ConstraintLayout {

    private static final String TAG = "MainKeyboardView";

    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private CircleOnPressListener leftCircleOnPressListener;
    private CircleOnPressListener rightCircleOnPressListener;

    private View keyboardView;

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
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyboardView = inflater.inflate(R.layout.key_layout, null);

        // Get buttons by id (defined in XML), and add listener functions to them
        keyboardView.findViewById(R.id.topLeftButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, getKeyMapFromConfigFile(R.raw.top_left)).getButtonCallback()
        );
        keyboardView.findViewById(R.id.topRightButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, getKeyMapFromConfigFile(R.raw.top_right)).getButtonCallback()
        );
        keyboardView.findViewById(R.id.bottomLeftButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, getKeyMapFromConfigFile(R.raw.bottom_left)).getButtonCallback()
        );
        keyboardView.findViewById(R.id.bottomRightButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, getKeyMapFromConfigFile(R.raw.bottom_right)).getButtonCallback()
        );
        keyboardView.findViewById(R.id.backspaceButton).setOnTouchListener(
                new BackspaceListener(context, circleKeyboardApplication).getButtonCallback()
        );
        keyboardView.findViewById(R.id.shiftButton).setOnTouchListener(
                new ShiftListener(context, circleKeyboardApplication, this).getButtonCallback()
        );
        keyboardView.findViewById(R.id.numButton).setOnTouchListener(
                new NumListener(context, circleKeyboardApplication).getButtonCallback()
        );
        keyboardView.findViewById(R.id.spaceButton).setOnTouchListener(
                new SpaceListener(context, circleKeyboardApplication).getButtonCallback()
        );
        keyboardView.findViewById(R.id.enterButton).setOnTouchListener(
                new EnterListener(context, circleKeyboardApplication).getButtonCallback()
        );
    }

    public void shift(boolean upperCase) {
        Log.d("", "Circle Shift");

        // Capitalize actual letters
        circleKeyboardApplication.shift(upperCase);

        // Capitalize visual letters
        toggleUpperCase(keyboardView.findViewById(R.id.key1), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key2), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key3), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key4), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key5), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key6), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key7), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key8), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key9), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key10), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key11), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key12), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key13), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key14), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key15), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key16), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key17), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key18), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key19), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key20), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key21), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key22), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key23), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key24), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key25), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key26), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key27), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key28), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key29), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key30), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key31), upperCase);
        toggleUpperCase(keyboardView.findViewById(R.id.key32), upperCase);
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

    public View getKeyboardView() {
        return keyboardView;
    }

    private KeyMap getKeyMapFromConfigFile(int fileName) {
        try {
            return parseConfigFile(fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error reading file");
            throw new RuntimeException("Can not parse file");
        }
    }

    private KeyMap parseConfigFile(int fileName) throws IOException {
        InputStream inputStream = null;
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            inputStream = getResources().openRawResource(fileName);
            return mapper.readValue(inputStream, KeyMap.class);
        } catch (IOException e) {
            Log.e(TAG, "Error reading file");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        throw new RuntimeException("Ca not parse file");
    }
}
