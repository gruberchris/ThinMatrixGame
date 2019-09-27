package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.engine.DisplayManager;
import com.chrisgruber.thinmatrixgame.engine.ModelLoader;
import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.Renderer;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.shaders.StaticShader;
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

        StaticShader staticShader = new StaticShader();
        staticShader.create();

        Renderer renderer = new Renderer(staticShader);

        /*
        float[] modelVertices = {
                -0.5f, 0.5f, 0f,        // V0
                -0.5f, -0.5f, 0f,       // V1
                0.5f, -0.5f, 0f,        // V2
                0.5f, 0.5f, 0f          // V3
        };

        int[] modelIndices = {
                0, 1, 3,                // Top left triangle (V0, V1, V3)
                3, 1, 2                 // Bottom right triangle (V3, V1, V2)
        };

        float[] textureCoords = {
                0, 0,                   // V0
                0, 1,                   // V1
                1, 1,                   // V2
                1, 0                    // V3
        };
        */

        float[] modelVertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0


        };

        int[] modelIndices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        RawModel model = modelLoader.loadToVao(modelVertices, textureCoords, modelIndices);
        ModelTexture modelTexture = new ModelTexture(modelLoader.loadTexture("resources/theman.png"));
        TexturedModel texturedModel = new TexturedModel(model, modelTexture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
        Camera camera = new Camera();

        while(DisplayManager.shouldDisplayClose()) {
            // move the entity and rotate each frame
            //entity.increasePosition(0, 0, -0.1f);
            entity.increaseRotation(1, 1, 0);

            camera.move();

            renderer.prepare();
            staticShader.bind();
            staticShader.loadViewMatrix(camera);
            renderer.render(entity, staticShader);
            staticShader.unbind();
            DisplayManager.updateDisplay();
        }

        staticShader.destroy();
        modelLoader.destroy();
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
