package com.chrisgruber.thinmatrixgame.engine.entities;

import com.chrisgruber.thinmatrixgame.engine.io.Mouse;
import com.chrisgruber.thinmatrixgame.engine.io.Keyboard;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;
    private final Player player;
    private float distanceFromPlayer;
    private float angleAroundPLayer;

    public Camera(Player player) {
        this.player = player;
        setCameraDefaultPosition();
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        checkInputs();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - (player.getRotationY() + angleAroundPLayer);
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

    private void calculateZoom() {
        // TODO: zoom velocity doesn't stop until player applies equal opposite zoom out input
        float zoomLevel = (float) Mouse.getMouseScrollY() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        // TODO: pitching adjustments don't stop until the mouse button is released!
        if (Mouse.isButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            float pitchChange = (float) Mouse.getMouseY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        // TODO: angleAroundPLayer adjustments don't stop until the mouse button is released!
        if (Mouse.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            float angleChange = (float) Mouse.getMouseX() * 0.3f;
            angleAroundPLayer -= angleChange;
        }
    }

    private void setCameraDefaultPosition() {
        position = new Vector3f(0, 0,0);
        distanceFromPlayer = 50;
        pitch = 20;
        yaw = 0;
        roll = 0;
        angleAroundPLayer = 0;
    }

    private void checkInputs() {
        // reset camera to default position
        if (Keyboard.isKeyDown(GLFW_KEY_HOME)) {
            setCameraDefaultPosition();
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistanceFromPlayer, float verticalDistanceFromPlayer) {
        float theta = player.getRotationY() + angleAroundPLayer;
        float offsetXOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.sin(Math.toRadians(theta)));
        float offsetZOfCameraFromPlayer = (float) (horizontalDistanceFromPlayer * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetXOfCameraFromPlayer;
        position.z = player.getPosition().z - offsetZOfCameraFromPlayer;
        position.y = player.getPosition().y + verticalDistanceFromPlayer;
    }
}
