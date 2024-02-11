package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;

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
    public Key getPropertyByIndex(int index) {
        Map<Integer, Key> properties = new HashMap<>();

        properties.put(0, this.up);
        properties.put(1, this.left);
        properties.put(2, this.down);
        properties.put(3, this.right);
        properties.put(4, this.farUp);
        properties.put(5, this.farLeft);
        properties.put(6, this.farDown);
        properties.put(7, this.farRight);

        return properties.get(index);
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
