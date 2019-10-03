package com.chrisgruber.thinmatrixgame.engine.textures;

public class ModelTexture {
    private int textureId;
    private float shineDamper;
    private float reflectivity;
    private boolean hasTransparency;
    private int numberOfRowsInTextureAtlas;

    // some models, like the grass model, needs fake lighting to look better as the model is a quad with normals facing in many different directions
    private boolean useFakeLighting;

    public ModelTexture(int textureId) {
        this.textureId = textureId;
        shineDamper = 1;
        numberOfRowsInTextureAtlas = 1;
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

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public int getNumberOfRowsInTextureAtlas() {
        return numberOfRowsInTextureAtlas;
    }

    public void setNumberOfRowsInTextureAtlas(int numberOfRowsInTextureAtlas) {
        this.numberOfRowsInTextureAtlas = numberOfRowsInTextureAtlas;
    }
}
