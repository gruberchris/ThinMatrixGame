package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.engine.*;
import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.entities.Light;
import com.chrisgruber.thinmatrixgame.engine.entities.Player;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.terrains.Terrain;
import com.chrisgruber.thinmatrixgame.engine.textures.ModelTexture;
import com.chrisgruber.thinmatrixgame.engine.textures.TerrainTexture;
import com.chrisgruber.thinmatrixgame.engine.textures.TerrainTexturePack;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public void start() {
        DisplayManager.createDisplay();
        DisplayManager.setShowFPSTitle(true);   // TODO: Debug only

        System.out.println("OpenGL: " + DisplayManager.getOpenGlVersionMessage());

        ModelLoader modelLoader = new ModelLoader();

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

        // Multitextured Terrain
        TerrainTexture backgroundTexture = new TerrainTexture(modelLoader.loadTexture("resources/grassy2.png"));
        TerrainTexture rTexture = new TerrainTexture(modelLoader.loadTexture("resources/mud.png"));
        TerrainTexture gTexture = new TerrainTexture(modelLoader.loadTexture("resources/grassFlowers.png"));
        TerrainTexture bTexture = new TerrainTexture(modelLoader.loadTexture("resources/path.png"));
        TerrainTexture blendMap = new TerrainTexture(modelLoader.loadTexture("resources/blendMap.png"));

        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Terrain entityList
        Terrain terrain = new Terrain(0, 0, modelLoader, terrainTexturePack, blendMap, "resources/heightmap.png");
        // Terrain terrain2 = new Terrain(1, 0, modelLoader, terrainTexturePack, blendMap, "resources/heightmap.png");

        List<Entity> entityList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 300; i++) {
            entityList.add(new Entity(treeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 5));
            entityList.add(new Entity(lowPolyTreeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entityList.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
            entityList.add(new Entity(fernModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
        }

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1,1));
        MasterRenderer masterRenderer = new MasterRenderer();

        TexturedModel bunnyModel = new TexturedModel(ObjLoader.loadObjModel("resources/stanfordBunny.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/white.png")));
        Player player = new Player(bunnyModel, new Vector3f(0, 0, -100), 0,0,0,1);
        Camera camera = new Camera(player);

        while (DisplayManager.shouldDisplayClose()) {
            camera.move();
            player.move();

            masterRenderer.processTerrain(terrain);
            // masterRenderer.processTerrain(terrain2);
            masterRenderer.processEntity(player);

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
