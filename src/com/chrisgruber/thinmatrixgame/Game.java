package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.graphics.DisplayManager;
import com.chrisgruber.thinmatrixgame.graphics.ModelLoader;
import com.chrisgruber.thinmatrixgame.graphics.RawModel;
import com.chrisgruber.thinmatrixgame.graphics.Renderer;

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

        RawModel model = modelLoader.loadToVao(modelVertices, modelIndices);

        while(DisplayManager.shouldDisplayClose()) {
            renderer.prepare();

            // TODO: game logic

            renderer.render(model);

            DisplayManager.updateDisplay();
        }

        modelLoader.Destroy();
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
