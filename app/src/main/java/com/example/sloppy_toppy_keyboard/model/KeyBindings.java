package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class KeyBindings {

    private KeyMap topLeft;
    private KeyMap topRight;
    private KeyMap bottomLeft;
    private KeyMap bottomRight;

    public KeyMap getTopLeft() {
        return topLeft;
    }

    public KeyMap getTopRight() {
        return topRight;
    }

    public KeyMap getBottomLeft() {
        return bottomLeft;
    }

    public KeyMap getBottomRight() {
        return bottomRight;
    }
}
