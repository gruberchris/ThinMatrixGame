package com.chrisgruber.thinmatrixgame.engine.models;

public class RawModel {
    final private int vaoId;
    final private int vertexCount;

    public RawModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
