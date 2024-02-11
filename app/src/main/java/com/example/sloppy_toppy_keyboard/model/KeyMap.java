package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class KeyMap {

    public KeyProperties left;
    public KeyProperties up;
    public KeyProperties right;
    public KeyProperties down;

    private KeyProperties farLeft;
    private KeyProperties farUp;
    private KeyProperties farRight;
    private KeyProperties farDown;

    private KeyProperties tap;
    private KeyProperties longPress;


    // Used to set TextView values dynamically from config file.
    // The numbers must match the order of the letters in the XML layout.
    public KeyProperties getPropertyByIndex(KeyMap keymap, int propertyIndex) {
        switch (propertyIndex) {
            case 0:
                return keymap.getUp();
            case 1:
                return keymap.getLeft();
            case 2:
                return keymap.getDown();
            case 3:
                return keymap.getRight();
            case 4:
                return keymap.getFarUp();
            case 5:
                return keymap.getFarLeft();
            case 6:
                return keymap.getFarDown();
            case 7:
                return keymap.getFarRight();
        }
        throw new RuntimeException("Unknown number for property");
    }

    public KeyProperties getLeft() {
        return left;
    }

    public KeyProperties getUp() {
        return up;
    }

    public KeyProperties getRight() {
        return right;
    }

    public KeyProperties getDown() {
        return down;
    }

    public KeyProperties getFarLeft() {
        return farLeft;
    }

    public KeyProperties getFarUp() {
        return farUp;
    }

    public KeyProperties getFarRight() {
        return farRight;
    }

    public KeyProperties getFarDown() {
        return farDown;
    }

    public KeyProperties getTap() {
        return tap;
    }

    public KeyProperties getLongPress() {
        return longPress;
    }
}
