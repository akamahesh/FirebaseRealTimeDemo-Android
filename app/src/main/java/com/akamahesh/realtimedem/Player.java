package com.akamahesh.realtimedem;

public class Player {
    private int id;
    private float x = 0.0f;
    private float y = 0.0f;

    Player() {

    }
    Player(int id) {
        this.id = id;
    }
    Player(int id,float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
