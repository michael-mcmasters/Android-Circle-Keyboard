package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sloppy_toppy_keyboard.listeners.BackspaceListener;
import com.example.sloppy_toppy_keyboard.listeners.ButtonListener;
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

        Button topLeftButton = keyboardView.findViewById(R.id.topLeftButton);
        Button topRightButton = keyboardView.findViewById(R.id.topRightButton);
        Button bottomLeftButton = keyboardView.findViewById(R.id.bottomLeftButton);
        Button bottomRightButton = keyboardView.findViewById(R.id.bottomRightButton);

        Button backspaceButton = keyboardView.findViewById(R.id.backspaceButton);
        Button numButton = keyboardView.findViewById(R.id.numButton);
        Button spaceButton = keyboardView.findViewById(R.id.spaceButton);
        Button enterButton = keyboardView.findViewById(R.id.enterButton);

        topLeftButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("A", "B", "C", "D")).getButtonCallback(topLeftButton)
        );
        topRightButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("E", "F", "G", "H")).getButtonCallback(topRightButton)
        );
        bottomLeftButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("I", "J", "K", "L")).getButtonCallback(bottomLeftButton)
        );
        bottomRightButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("M", "N", "O", "P")).getButtonCallback(bottomRightButton)
        );
        backspaceButton.setOnTouchListener(
                new BackspaceListener(context, circleKeyboardApplication).getButtonCallback()
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
