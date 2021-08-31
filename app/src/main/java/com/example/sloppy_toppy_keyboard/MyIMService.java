package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class MyIMService extends InputMethodService implements View.OnClickListener {

    private static String TAG = "MyIMService";

    @Override
    public View onCreateInputView() {
        View myKeyboardView = getLayoutInflater().inflate(R.layout.key_layout, null);
        Button btnA = myKeyboardView.findViewById(R.id.btnA);
        btnA.setOnClickListener(this);
        Button button2 = myKeyboardView.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        //ADD ALL THE OTHER LISTENERS HERE FOR ALL THE KEYS
        return myKeyboardView;
    }

    @Override
    public void onClick(View v) {
        //handle all the keyboard key clicks here

        InputConnection ic = getCurrentInputConnection();
        if (v instanceof Button) {
            String clickedKeyText = ((Button) v).getText().toString();
            //ic.commitText(clickedKeyText, 1);
            ic.commitText("hiya", 1);
        }
    }
}
