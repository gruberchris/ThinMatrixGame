package com.chrisgruber.thinmatrixgame.engine;

import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.shaders.StaticShader;
import com.chrisgruber.thinmatrixgame.engine.textures.ModelTexture;
import com.chrisgruber.thinmatrixgame.engine.utils.Maths;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class EntityRenderer {
    private StaticShader staticShader;

    public EntityRenderer(StaticShader staticShader, Matrix4f projectionMatrix) {
        if (staticShader == null) {
            throw new IllegalArgumentException("staticShader argument has not been initialized!");
        }

        if (projectionMatrix == null) {
            throw new IllegalArgumentException("projectionMatrix argument has not been initialized!");
        }

        this.staticShader = staticShader;
        staticShader.bind();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.unbind();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> entityList = entities.get(texturedModel);

            for (Entity entity : entityList) {
                prepareEntity(entity);
                glDrawElements(GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
            }

            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();

        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // VAO 0 = vertex spacial coordinates
        glEnableVertexAttribArray(1);   // VAO 1 = texture coordinates
        glEnableVertexAttribArray(2);   // VAO 2 = normals

        ModelTexture texture = texturedModel.getModelTexture();

        staticShader.loadNumberOfRowsInTextureAtlas(texture.getNumberOfRowsInTextureAtlas());

        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }

        staticShader.loadFakeLighting(texture.isUseFakeLighting());
        staticShader.loadSpecularLight(texture.getShineDamper(), texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        int textureId = texturedModel.getModelTexture().getTextureId();
        glBindTexture(GL_TEXTURE_2D, textureId);    // sampler2D in fragment shader  uses texture bank 0 by default
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling(); // make sure culling is enabled for the next model that renders
        glDisableVertexAttribArray(0);  // VAO 0 = vertex spacial coordinates
        glDisableVertexAttribArray(1);  // VAO 1 = texture coordinates
        glDisableVertexAttribArray(2);  // VAO 2 = normals
        glBindVertexArray(0);   // Unbind the VAO
    }

    private void prepareEntity(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        staticShader.loadTransformationMatrix(transformationMatrix);
        staticShader.loadOffset(entity.getTextureAtlasXOffset(), entity.getTextureAtlasYOffset());
    }
}
