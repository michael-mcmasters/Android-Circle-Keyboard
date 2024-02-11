package com.example.sloppy_toppy_keyboard.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class KeyProperties {

    private String visual;
    private String functional;


    public String getVisual() {
        return visual;
    }

    public String getFunctional() {
        return functional;
    }
}
