package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.engine.*;
import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.entities.Light;
import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.terrains.Terrain;
import com.chrisgruber.thinmatrixgame.engine.textures.ModelTexture;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Runnable {
    private Thread gameThread;

    public void start() {
        gameThread = new Thread(this, "GameThread");
        gameThread.start();
    }

    @Override
    public void run() {
        DisplayManager.createDisplay();
        DisplayManager.setShowFPSTitle(true);   // TODO: Debug only

        System.out.println("OpenGL: " + DisplayManager.getOpenGlVersionMessage());

        ModelLoader modelLoader = new ModelLoader();

        // The "stall" entity
        RawModel model = ObjLoader.loadObjModel("resources/stall.obj", modelLoader);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(modelLoader.loadTexture("resources/stallTexture.png")));
        ModelTexture modelTexture = texturedModel.getModelTexture();
        modelTexture.setShineDamper(10);
        modelTexture.setReflectivity(1);
        Entity stallEntity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);

        // Tree entity
        TexturedModel treeModel = new TexturedModel(ObjLoader.loadObjModel("resources/tree.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/tree.png")));

        // Low poly tree entity
        TexturedModel lowPolyTreeModel = new TexturedModel(ObjLoader.loadObjModel("resources/lowPolyTree.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/lowPolyTree.png")));

        // Grass entity
        TexturedModel grassModel = new TexturedModel(ObjLoader.loadObjModel("resources/grassModel.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/grassTexture.png")));
        grassModel.getModelTexture().setHasTransparency(true);
        grassModel.getModelTexture().setUseFakeLighting(true);

        // Fern entity
        TexturedModel fernModel = new TexturedModel(ObjLoader.loadObjModel("resources/fern.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/fern.png")));
        fernModel.getModelTexture().setHasTransparency(true);

        // Terrain entityList
        Terrain terrain = new Terrain(0,0, modelLoader, new ModelTexture(modelLoader.loadTexture("resources/grass.png")));
        Terrain terrain2 = new Terrain(1,0, modelLoader, new ModelTexture(modelLoader.loadTexture("resources/grass.png")));

        List<Entity> entityList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 500; i++) {
            entityList.add(new Entity(treeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 5));
            entityList.add(new Entity(lowPolyTreeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entityList.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entityList.add(new Entity(fernModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
        }

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1,1));
        Camera camera = new Camera(0, 5, 0);
        MasterRenderer masterRenderer = new MasterRenderer();

        while(DisplayManager.shouldDisplayClose()) {
            // move the entity and rotate each frame
            // entity.increasePosition(0, 0, -0.1f);
            stallEntity.increaseRotation(0, 1, 0);
            camera.move();

            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);
            masterRenderer.processEntity(stallEntity);

            for (Entity entity : entityList) {
                masterRenderer.processEntity(entity);
            }

            masterRenderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        masterRenderer.destory();
        modelLoader.destroy();
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
