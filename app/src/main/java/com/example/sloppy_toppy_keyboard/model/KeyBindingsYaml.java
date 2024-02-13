package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class KeyBindingsYaml {

    private ButtonKeyBindings topLeft;
    private ButtonKeyBindings topRight;
    private ButtonKeyBindings bottomLeft;
    private ButtonKeyBindings bottomRight;
    private Base base;

    public ButtonKeyBindings getTopLeftButtonKeyMap() {
        return topLeft;
    }

    public ButtonKeyBindings getTopRightButtonKeyMap() {
        return topRight;
    }

    public ButtonKeyBindings getBottomLeftButtonKeyMap() {
        return bottomLeft;
    }

    public ButtonKeyBindings getBottomRightButtonKeyMap() {
        return bottomRight;
    }

    public Base getBaseKeyMap() {
        return base;
    }

}
