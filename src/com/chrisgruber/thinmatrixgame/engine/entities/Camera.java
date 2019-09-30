package com.chrisgruber.thinmatrixgame.engine.entities;

import com.chrisgruber.thinmatrixgame.engine.io.Keyboard;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
        position = new Vector3f(0, 0,0);
    }

    public Camera(int x, int y, int z) {
        position = new Vector3f(x, y, z);
    }

    public Camera(int x, int y, int z, float pitch, float yaw, float roll) {
        position = new Vector3f(x, y, z);
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public void move() {
        if (Keyboard.isKeyDown(GLFW_KEY_PAGE_UP)) {
            pitch -= 1;
        } else if (Keyboard.isKeyDown(GLFW_KEY_PAGE_DOWN)) {
            pitch += 1;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setX(int x) {
        position.x = x;
    }

    public void setY(int y) {
        position.y = y;
    }

    public void setZ(int z) {
        position.z = z;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
