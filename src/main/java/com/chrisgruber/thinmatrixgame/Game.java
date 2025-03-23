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
    Random random = new Random();

    private void start() {
        DisplayManager.createDisplay();

        // Show FPS title only if debugging
        if (java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp")) {
            DisplayManager.setShowFPSTitle(true);
        }

        System.out.println("OpenGL: " + DisplayManager.getOpenGlVersionMessage());
        System.out.println("LWJGL: " + DisplayManager.getLwjglVersionMessage());

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
        ModelTexture fernTextureAtlas = new ModelTexture(modelLoader.loadTexture("resources/fern.png"));
        fernTextureAtlas.setNumberOfRowsInTextureAtlas(2);
        TexturedModel fernModel = new TexturedModel(ObjLoader.loadObjModel("resources/fern.obj", modelLoader), fernTextureAtlas);
        fernModel.getModelTexture().setHasTransparency(true);

        // Multi-textured Terrain
        TerrainTexture backgroundTexture = new TerrainTexture(modelLoader.loadTexture("resources/grassy2.png"));
        TerrainTexture rTexture = new TerrainTexture(modelLoader.loadTexture("resources/mud.png"));
        TerrainTexture gTexture = new TerrainTexture(modelLoader.loadTexture("resources/grassFlowers.png"));
        TerrainTexture bTexture = new TerrainTexture(modelLoader.loadTexture("resources/path.png"));
        TerrainTexture blendMap = new TerrainTexture(modelLoader.loadTexture("resources/blendMap.png"));

        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        // Terrain entityList
        Terrain terrain = new Terrain(0, -1, modelLoader, terrainTexturePack, blendMap, "resources/heightmap.png");
        Terrain terrain2 = new Terrain(-1, -1, modelLoader, terrainTexturePack, blendMap, "resources/heightmap.png");

        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain);
        terrains.add(terrain2);

        List<Entity> entityList = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            if (i % 20 == 0) {
                // Low poly trees
                float x = this.random.nextFloat() * 800 - 400;
                float z = this.random.nextFloat() * -600;

                Terrain currentTerrain = getTerrainAt(x, z, terrains);
                if (currentTerrain != null) {
                    float y = currentTerrain.getTerrainHeightForSinglePoint(x, z);
                    entityList.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), 0, this.random.nextFloat() * 360, 0, 1));
                }
            }

            if (i % 20 == 0) {
                // Trees
                float x = this.random.nextFloat() * 800 - 400;
                float z = this.random.nextFloat() * -600;

                Terrain currentTerrain = getTerrainAt(x, z, terrains);
                if (currentTerrain != null) {
                    float y = currentTerrain.getTerrainHeightForSinglePoint(x, z);
                    entityList.add(new Entity(treeModel, new Vector3f(x, y, z), 0, this.random.nextFloat() * 360, 0, 5));
                }
            }

            if (i % 10 == 0) {
                // Ferns
                float x = this.random.nextFloat() * 800 - 400;
                float z = this.random.nextFloat() * -600;

                Terrain currentTerrain = getTerrainAt(x, z, terrains);
                if (currentTerrain != null) {
                    float y = currentTerrain.getTerrainHeightForSinglePoint(x, z);
                    entityList.add(new Entity(fernModel, this.random.nextInt(4), new Vector3f(x, y, z), 0, this.random.nextFloat() * 360, 0, 0.9f));
                }
            }

            if (i % 5 == 0) {
                // Grass
                float x = this.random.nextFloat() * 800 - 400;
                float z = this.random.nextFloat() * -600;

                Terrain currentTerrain = getTerrainAt(x, z, terrains);
                if (currentTerrain != null) {
                    // Get exact height at this point
                    float y = currentTerrain.getTerrainHeightForSinglePoint(x, z);

                    // Add two adjustments:
                    // 1. Consistent vertical offset to embed grass base into terrain
                    // 2. Small random variation to prevent uniform horizontal placement
                    float offset = -0.5f;
                    float heightVariation = this.random.nextFloat() * 0.3f;

                    entityList.add(new Entity(
                            grassModel,
                            new Vector3f(x, y + offset - heightVariation, z),
                            0,
                            this.random.nextFloat() * 360,
                            0,
                            0.8f + this.random.nextFloat() * 0.4f  // Slight scale variation
                    ));
                }
            }
        }

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1,1));
        MasterRenderer masterRenderer = new MasterRenderer();

        TexturedModel bunnyModel = new TexturedModel(ObjLoader.loadObjModel("resources/stanfordBunny.obj", modelLoader), new ModelTexture(modelLoader.loadTexture("resources/white.png")));
        Player player = new Player(bunnyModel, new Vector3f(100, 0, -100), 0,0,0,0.6f);
        Camera camera = new Camera(player);

        while (DisplayManager.shouldDisplayClose()) {
            //player.move(terrain);   // to do this with multiple Terrain, need to test first to know which Terrain the player's position is in
            Terrain playerTerrain = getTerrainAt(player.getPosition().x, player.getPosition().z, terrains);
            if (playerTerrain != null) {
                player.move(playerTerrain);
            } else {
                // Handle case when player is outside terrain
                System.out.println("Player " + player.getPosition() + " not found");
            }

            camera.move();

            masterRenderer.processEntity(player);
            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);

            for (Entity entity : entityList) {
                masterRenderer.processEntity(entity);
            }

            masterRenderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        masterRenderer.destroy();
        modelLoader.destroy();
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        new Game().start();
    }

    private Terrain getTerrainAt(float worldX, float worldZ, List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            // Check if position is within this terrain's bounds
            float terrainX = terrain.getX();
            float terrainZ = terrain.getZ();
            //float size = terrain.getSIZE();
            float size = 800;

            if (worldX >= terrainX && worldX < terrainX + size &&
                    worldZ >= terrainZ && worldZ < terrainZ + size) {
                return terrain;
            }
        }

        return null; // No terrain found
    }
}