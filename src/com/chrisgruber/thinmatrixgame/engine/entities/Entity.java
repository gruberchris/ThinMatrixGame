package com.chrisgruber.thinmatrixgame.engine.entities;

import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotationX, rotationY, rotationZ;
    private float scale;
    private int textureAtlasIndex;

    public Entity(TexturedModel texturedModel, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
    }

    public Entity(TexturedModel texturedModel, int textureAtlasIndex, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
        this.textureAtlasIndex = textureAtlasIndex;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotationX += dx;
        this.rotationY += dy;
        this.rotationZ += dz;
    }

    public float getTextureAtlasXOffset() {
        int column = textureAtlasIndex % texturedModel.getModelTexture().getNumberOfRowsInTextureAtlas();
        return (float) column / (float) texturedModel.getModelTexture().getNumberOfRowsInTextureAtlas();
    }

    public float getTextureAtlasYOffset() {
        int row = textureAtlasIndex / texturedModel.getModelTexture().getNumberOfRowsInTextureAtlas();
        return (float) row / (float) texturedModel.getModelTexture().getNumberOfRowsInTextureAtlas();
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
