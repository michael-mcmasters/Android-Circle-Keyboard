package com.example.sloppy_toppy_keyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainKeyboardView extends ConstraintLayout {

    private static final String TAG = "MainKeyboardView";
    private Context context;
    private CircleKeyboardApplication circleKeyboardApplication;
    private CircleOnPressListener leftCircleOnPressListener;
    private CircleOnPressListener rightCircleOnPressListener;
    private View keyboardView;


    public MainKeyboardView(Context context, CircleKeyboardApplication circleKeyboardApplication) {
        super(context);
        this.context = context;
        leftCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, true);
        rightCircleOnPressListener = new CircleOnPressListener(context, circleKeyboardApplication, false);
        initialize();
    }

    public View getKeyboardView() {
        return keyboardView;
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyboardView = inflater.inflate(R.layout.key_layout, null);

        Button button0 = keyboardView.findViewById(R.id.button0);
        Button button1 = keyboardView.findViewById(R.id.button1);
        button0.setOnTouchListener(leftCircleOnPressListener.getButtonCallback(button0));
        button1.setOnTouchListener(rightCircleOnPressListener.getButtonCallback(button1));
    }
}
