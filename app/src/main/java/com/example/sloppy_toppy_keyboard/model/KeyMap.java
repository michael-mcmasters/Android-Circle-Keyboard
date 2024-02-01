package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

// Allows Jackson ObjectMapper to set private fields (if any)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class KeyMap {

    public String left;
    public String up;
    public String right;
    public String down;

    private String farLeft;
    private String farUp;
    private String farRight;
    private String farDown;


    public KeyMap() {

    }

    public KeyMap(String left, String up, String right, String down, String farLeft, String farUp, String farRight, String farDown) {
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
        this.farLeft = farLeft;
        this.farUp = farUp;
        this.farRight = farRight;
        this.farDown = farDown;
    }

    // Used to set TextView values dynamically from config file.
    // The numbers must match the order of the letters in the XML layout.
    public String getPropertyValueByIndex(KeyMap keymap, int propertyIndex) {
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

    public String getLeft() {
        return left;
    }

    public String getUp() {
        return up;
    }

    public String getRight() {
        return right;
    }

    public String getDown() {
        return down;
    }

    public String getFarLeft() {
        return farLeft;
    }

    public String getFarUp() {
        return farUp;
    }

    public String getFarRight() {
        return farRight;
    }

    public String getFarDown() {
        return farDown;
    }
}
