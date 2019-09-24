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

        // TODO: temp model. Delete later
        float[] modelVertices = {
                // Left bottom triangle
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                // Right top triangle
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        RawModel model = modelLoader.loadToVao(modelVertices);

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
