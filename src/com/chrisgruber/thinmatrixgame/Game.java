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
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);

        // Terrain entities
        Terrain terrain = new Terrain(0,0, modelLoader, new ModelTexture(modelLoader.loadTexture("resources/grass.png")));
        Terrain terrain2 = new Terrain(1,0, modelLoader, new ModelTexture(modelLoader.loadTexture("resources/grass.png")));

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1,1));
        Camera camera = new Camera(0, 5, 0);
        MasterRenderer masterRenderer = new MasterRenderer();

        while(DisplayManager.shouldDisplayClose()) {
            // move the entity and rotate each frame
            // entity.increasePosition(0, 0, -0.1f);
            entity.increaseRotation(0, 1, 0);
            camera.move();

            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);
            masterRenderer.processEntity(entity);

            /*
            for (Entity cubeEntity : cubeEntityList) {
                masterRenderer.processEntity(cubeEntity);
            }
            */

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
