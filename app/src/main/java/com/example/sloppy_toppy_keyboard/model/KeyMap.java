package com.example.sloppy_toppy_keyboard.model;

public class KeyMap {

    private String left;
    private String up;
    private String right;
    private String down;

    public KeyMap(String left, String up, String right, String down) {
        this.left = left;
        this.up = up;
        this.right = right;
        this.down = down;
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
}
