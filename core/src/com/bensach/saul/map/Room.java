package com.bensach.saul.map;

/**
 * Created by saul- on 18/04/2016.
 */
public class Room {

    private int x, y, width, height;

    public Room(int x, int height, int width, int y) {
        this.x = x;
        this.height = height;
        this.width = width;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getY() {
        return y;
    }

}
