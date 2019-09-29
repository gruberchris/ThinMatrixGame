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
        final float MOVE_RATE = 0.25f;

        if (Keyboard.isKeyDown(GLFW_KEY_W)) {
            position.z -= MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_S)) {
            position.z += MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_D)) {
            position.x += MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_A)) {
            position.x -= MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_Q)) {
            position.y += MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_E)) {
            position.y -= MOVE_RATE;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_Z)) {
            position.x = 0;
            position.y = 0;
            position.z = 0;
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
