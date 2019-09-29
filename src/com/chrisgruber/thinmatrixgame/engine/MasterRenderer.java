package com.chrisgruber.thinmatrixgame.engine;

import com.chrisgruber.thinmatrixgame.engine.entities.Camera;
import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.entities.Light;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    private StaticShader staticShader;
    private Renderer renderer;
    private Map<TexturedModel, List<Entity>> entities;

    public MasterRenderer() {
        staticShader = new StaticShader();
        staticShader.create();

        renderer = new Renderer(staticShader);
        entities = new HashMap<>();
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
        renderer.prepare();
        staticShader.bind();
        staticShader.loadDiffuseLight(light);
        staticShader.loadViewMatrix(camera);
        renderer.render(entities);
        staticShader.unbind();
        entities.clear();
    }

    public void destory() {
        staticShader.destroy();
    }
}
