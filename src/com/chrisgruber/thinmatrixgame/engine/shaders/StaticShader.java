package com.chrisgruber.thinmatrixgame.engine.shaders;

import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Light;
import com.chrisgruber.thinmatrixgame.engine.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class StaticShader extends ShaderProgramBase {
    private static final String VERTEX_FILE = "resources/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "resources/shaders/fragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numberOfRowsInTextureAtlas;
    private int location_offset;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_numberOfRowsInTextureAtlas = super.getUniformLocation("numberOfRowsInTextureAtlas");   // texture atlas support
        location_offset = super.getUniformLocation("offset");   // texture atlas support
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadDiffuseLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadSpecularLight(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void loadNumberOfRowsInTextureAtlas(int numberOfRowsInTextureAtlas) {
        super.loadFloat(location_numberOfRowsInTextureAtlas, numberOfRowsInTextureAtlas);
    }

    public void loadOffset(float x, float y) {
        super.loadVector(location_offset, new Vector2f(x, y));
    }
}
