package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ButtonKeyBindings {

    public Key left;
    public Key up;
    public Key right;
    public Key down;

    private Key farLeft;
    private Key farUp;
    private Key farRight;
    private Key farDown;

    private Key tap;
    private Key longPress;


    // Used to set TextView values dynamically from config file.
    // The numbers must match the order of the letters in the XML layout.
    public Key getPropertyByIndex(int propertyIndex) {
        switch (propertyIndex) {
            case 0:
                return this.up;
            case 1:
                return this.left;
            case 2:
                return this.down;
            case 3:
                return this.right;
            case 4:
                return this.farUp;
            case 5:
                return this.farLeft;
            case 6:
                return this.farDown;
            case 7:
                return this.farRight;
        }
        throw new RuntimeException("Unknown number for property");
    }

    public Key getLeft() {
        return left;
    }

    public Key getUp() {
        return up;
    }

    public Key getRight() {
        return right;
    }

    public Key getDown() {
        return down;
    }

    public Key getFarLeft() {
        return farLeft;
    }

    public Key getFarUp() {
        return farUp;
    }

    public Key getFarRight() {
        return farRight;
    }

    public Key getFarDown() {
        return farDown;
    }

    public Key getTap() {
        return tap;
    }

    public Key getLongPress() {
        return longPress;
    }
}
