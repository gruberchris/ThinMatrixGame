package com.chrisgruber.thinmatrixgame.graphics.shaders;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgramBase {
    private static final String VERTEX_FILE = "resources/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "resources/shaders/fragmentShader.glsl";

    private int location_transformationMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
}
