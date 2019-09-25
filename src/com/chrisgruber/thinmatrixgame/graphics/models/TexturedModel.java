package com.chrisgruber.thinmatrixgame.graphics.models;

import com.chrisgruber.thinmatrixgame.graphics.textures.ModelTexture;

public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture modelTexture;

    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
