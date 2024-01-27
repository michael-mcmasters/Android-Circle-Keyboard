package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
import com.example.sloppy_toppy_keyboard.listeners.EnterListener;
import com.example.sloppy_toppy_keyboard.listeners.NumListener;
import com.example.sloppy_toppy_keyboard.listeners.SpaceListener;
import com.example.sloppy_toppy_keyboard.model.KeyMap;
import com.example.sloppy_toppy_keyboard.old.CircleOnPressListener;

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
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("A", "B", "C", "D")).getButtonCallback()
        );
        keyboardView.findViewById(R.id.topRightButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("E", "F", "G", "H")).getButtonCallback()
        );
        keyboardView.findViewById(R.id.bottomLeftButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("I", "J", "K", "L")).getButtonCallback()
        );
        keyboardView.findViewById(R.id.bottomRightButton).setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("M", "N", "O", "P")).getButtonCallback()
        );
        keyboardView.findViewById(R.id.backspaceButton).setOnTouchListener(
                new BackspaceListener(context, circleKeyboardApplication).getButtonCallback()
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

    // This method maybe shouldn't be in this View class. Should create another class that instantiates this view class and the CircleOnPressListener class?
    public void notifyButtonState(boolean leftCircle, String circleState) {
        if (leftCircle) {
            leftCircleState = circleState;
        } else {
            rightCircleState = circleState;
        }

        if (leftCircleState.equals("ACTION_UP") && rightCircleState.equals("ACTION_UP")) {
            circleKeyboardApplication.commitText(" ");
        }
    }

    public View getKeyboardView() {
        return keyboardView;
    }
}
