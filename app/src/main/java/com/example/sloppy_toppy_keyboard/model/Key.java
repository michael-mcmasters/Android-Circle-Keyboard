package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Key {

    private String visual;              // how the key appears on the keyboard
    private String functional;          // what is actually inputted when "typing" this Key


    public String getVisual() {
        return visual;
    }

    public String getFunctional() {
        return functional;
    }
}
