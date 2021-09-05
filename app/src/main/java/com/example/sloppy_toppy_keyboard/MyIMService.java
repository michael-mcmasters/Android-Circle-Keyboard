package com.example.sloppy_toppy_keyboard;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;

public class MyIMService extends InputMethodService implements View.OnClickListener {

    private static String TAG = "MyIMService";

    private static class Vector2 {
        private double x;
        private double y;

        public Vector2 (float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public View onCreateInputView() {
        View myKeyboardView = getLayoutInflater().inflate(R.layout.key_layout, null);

        Button button0 = myKeyboardView.findViewById(R.id.button0);
        button0.setOnClickListener(this);

        Button button1 = myKeyboardView.findViewById(R.id.button1);
        button1.setOnClickListener(this);

        // On touch, get finger's current position, get dot rotation from center, that is the key being pressed.

        InputConnection ic = getCurrentInputConnection();

        View.OnTouchListener onTouchListenerCallback = (view, motion) -> {
            motion.getOrientation();
            //float position = motion.getX(0);
            //Log.d(TAG, "onCreateInputView: " + position);

            Vector2 viewVector = new Vector2(view.getX(), view.getY());
            Vector2 touchVector = new Vector2(motion.getX(), motion.getY());

            double distance = Math.hypot(viewVector.x - touchVector.x, touchVector.y - viewVector.y);

            if (distance > -40 && distance < 40) {
                ic.commitText("hiya", 1);
            }



//            Math.hypot(x1 - x2, y1 - y2);
//
//            double distance = Math.sqrt(Math.pow(touchVector.x - viewVector.x), 2) + Math.pow(motion.getY(1) - motion.getY(0), 2));
//
//            double distance = Math.sqrt(Math.pow(motion.getX(1) - motion.getX(0), 2) + Math.pow(motion.getY(1) - motion.getY(0), 2));
            Log.d(TAG, "onCreateInputView: " + distance);

            int action = motion.getActionMasked();
            switch(action) {
                case (MotionEvent.ACTION_HOVER_ENTER):
                    Log.d(TAG, "action hover enter");
                    return true;
                case (MotionEvent.ACTION_OUTSIDE):
                    Log.d(TAG, "action not outsie");
                    return true;
                case (MotionEvent.AXIS_RELATIVE_X):
                    Log.d(TAG, "action not outsie");
                    return true;
                default:
                    return true;
            }

//            float position = motion.getRawX(0);
//            Log.d(TAG, String.valueOf(position));
//            Vector2 vector = new Vector2();
//            Vector v;
        };

        button0.setOnTouchListener(onTouchListenerCallback);
        button1.setOnTouchListener(onTouchListenerCallback);


//        button1.setOnTouchListener((view, motion) -> {
////            onGenericMotionEvent()
//            Log.d(TAG, "onCreateInputView: view");
//            Log.d(TAG, "onCreateInputView: motion");
//            return true;
//        });
//        button2.setOnHoverListener((view, motion) -> {
//            Log.d(TAG, "onCreateInputView: view");
//            Log.d(TAG, "onCreateInputView: motion");
//            return true;
//        });
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
