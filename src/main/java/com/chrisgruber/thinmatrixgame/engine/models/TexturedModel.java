package com.chrisgruber.thinmatrixgame.engine.models;

import com.chrisgruber.thinmatrixgame.engine.textures.ModelTexture;

public class TexturedModel {
    final private RawModel rawModel;
    final private ModelTexture modelTexture;

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
