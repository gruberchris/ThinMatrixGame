package com.chrisgruber.thinmatrixgame.engine.textures;

public class ModelTexture {
    private int textureId;
    private float shineDamper;
    private float reflectivity;

    public ModelTexture(int textureId) {
        this.textureId = textureId;
        shineDamper = 1;
    }

    public int getTextureId() {
        return textureId;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
