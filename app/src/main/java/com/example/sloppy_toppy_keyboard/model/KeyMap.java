package com.example.sloppy_toppy_keyboard.model;

public class KeyMap {

    private String left;
    private String up;
    private String right;
    private String down;

    private String farLeft;
    private String farUp;
    private String farRight;
    private String farDown;


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
