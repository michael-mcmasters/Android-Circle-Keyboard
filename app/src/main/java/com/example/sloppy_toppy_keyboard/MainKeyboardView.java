package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sloppy_toppy_keyboard.model.KeyMap;

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

        topLeftButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("L", "U", "R", "D")).getButtonCallback(topLeftButton)
        );
        topRightButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("1", "2", "3", "4")).getButtonCallback(topRightButton)
        );
        bottomLeftButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("5", "6", "7", "8")).getButtonCallback(bottomLeftButton)
        );
        bottomRightButton.setOnTouchListener(
                new ButtonListener(context, circleKeyboardApplication, new KeyMap("9", "10", "11", "12")).getButtonCallback(bottomRightButton)
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
