package com.chrisgruber.thinmatrixgame.engine.entities;

import com.chrisgruber.thinmatrixgame.engine.DisplayManager;
import com.chrisgruber.thinmatrixgame.engine.io.Keyboard;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final float RUN_SPEED = 20;  // units per second
    private static final float TURN_SPEED = 160;    // degrees per second
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;  // assumes 100% flat terrain

    private Vector3f position;
    private float currentSpeed;
    private float currentTurnSpeed;
    private float upwardsSpeed;

    public Player(TexturedModel texturedModel, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        super(texturedModel, position, rotationX, rotationY, rotationZ, scale);
    }

    public void move() {
        checkInputs();

        // Calculate movement
        super.increaseRotation(0, currentTurnSpeed * (float) DisplayManager.getDeltaInSeconds(), 0);
        float distance = currentSpeed * (float) DisplayManager.getDeltaInSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotationY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotationY())));
        super.increasePosition(dx, 0, dz);

        // Calculate jump
        upwardsSpeed += GRAVITY * DisplayManager.getDeltaInSeconds();
        super.increasePosition(0, (float) (upwardsSpeed * DisplayManager.getDeltaInSeconds()), 0);
        if (super.getPosition().y < TERRAIN_HEIGHT) {
            upwardsSpeed = 0;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }

    private void jump() {
        if (upwardsSpeed == 0) {
            upwardsSpeed = JUMP_POWER;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(GLFW_KEY_W) || Keyboard.isKeyDown(GLFW_KEY_UP)) {
            currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_S) || Keyboard.isKeyDown(GLFW_KEY_DOWN)) {
            currentSpeed = -RUN_SPEED;
        } else {
            currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_D) || Keyboard.isKeyDown(GLFW_KEY_RIGHT)) {
            currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(GLFW_KEY_A) || Keyboard.isKeyDown(GLFW_KEY_LEFT)) {
            currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
            jump();
        }
    }
}
