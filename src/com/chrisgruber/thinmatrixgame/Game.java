package com.chrisgruber.thinmatrixgame;

import com.chrisgruber.thinmatrixgame.engine.DisplayManager;
import com.chrisgruber.thinmatrixgame.engine.ModelLoader;
import com.chrisgruber.thinmatrixgame.engine.ObjLoader;
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

        RawModel model = ObjLoader.loadObjModel("resources/stall.obj", modelLoader);
        ModelTexture modelTexture = new ModelTexture(modelLoader.loadTexture("resources/stallTexture.png"));
        TexturedModel texturedModel = new TexturedModel(model, modelTexture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, -2.5f, -15), 0, 0, 0, 1);
        Camera camera = new Camera();

        while(DisplayManager.shouldDisplayClose()) {
            // move the entity and rotate each frame
            // entity.increasePosition(0, 0, -0.1f);
            entity.increaseRotation(0, 1, 0);

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
