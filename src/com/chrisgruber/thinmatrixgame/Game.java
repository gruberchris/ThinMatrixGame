package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.graphics.DisplayManager;
import com.chrisgruber.thinmatrixgame.graphics.ModelLoader;
import com.chrisgruber.thinmatrixgame.graphics.models.RawModel;
import com.chrisgruber.thinmatrixgame.graphics.Renderer;
import com.chrisgruber.thinmatrixgame.graphics.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.graphics.shaders.StaticShader;
import com.chrisgruber.thinmatrixgame.graphics.textures.ModelTexture;

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
        Renderer renderer = new Renderer();

        StaticShader staticShader = new StaticShader();
        staticShader.create();

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

        RawModel model = modelLoader.loadToVao(modelVertices, textureCoords, modelIndices);
        ModelTexture modelTexture = new ModelTexture(modelLoader.loadTexture("resources/theman.png"));
        TexturedModel texturedModel = new TexturedModel(model, modelTexture);

        while(DisplayManager.shouldDisplayClose()) {
            renderer.prepare();
            staticShader.bind();
            renderer.render(texturedModel);
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
