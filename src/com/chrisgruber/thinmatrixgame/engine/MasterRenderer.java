package com.chrisgruber.thinmatrixgame.engine;

import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.entities.Light;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.shaders.StaticShader;
import com.chrisgruber.thinmatrixgame.engine.shaders.TerrainShader;
import com.chrisgruber.thinmatrixgame.engine.terrains.Terrain;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    private static final float SKY_RED = 0.95f;
    private static final float SKY_GREEN = 0.9f;
    private static final float SKY_BLUE = 0.67f;

    private StaticShader staticShader;
    private EntityRenderer entityRenderer;
    private Map<TexturedModel, List<Entity>> entities;
    private Matrix4f projectionMatrix;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader;
    private List<Terrain> terrainList;

    public MasterRenderer() {
        enableCulling();
        staticShader = new StaticShader();
        terrainShader = new TerrainShader();
        entities = new HashMap<>();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        terrainList = new ArrayList<>();
    }

    public static void enableCulling() {
        // don't texture surfaces with normal vectors facing away from the "camera". don't render back faces of the a model
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getTexturedModel();
        List<Entity> entityList = entities.get(entityModel);

        if (entityList == null) {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entityModel, newEntityList);
            return;
        }

        entityList.add(entity);
    }

    public void render(Light light, Camera camera) {
        prepare();
        staticShader.bind();
        staticShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        staticShader.loadDiffuseLight(light);
        staticShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        staticShader.unbind();
        terrainShader.bind();
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadDiffuseLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrainList);
        terrainShader.unbind();
        entities.clear();
        terrainList.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrainList.add(terrain);
    }

    public void destory() {
        staticShader.destroy();
        terrainShader.destroy();
    }

    private void prepare() {
        glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear screen and draw with color in color buffer
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWindowWidth() / (float) DisplayManager.getWindowHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
