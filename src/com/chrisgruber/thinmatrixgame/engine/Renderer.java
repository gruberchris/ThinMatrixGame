package com.chrisgruber.thinmatrixgame.engine;

import com.chrisgruber.thinmatrixgame.engine.entities.Entity;
import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.engine.shaders.StaticShader;
import com.chrisgruber.thinmatrixgame.engine.textures.ModelTexture;
import com.chrisgruber.thinmatrixgame.engine.utils.Maths;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader staticShader) {
        // don't texture surfaces with normal vectors facing away from the "camera". don't render back faces of the a model
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        createProjectionMatrix();
        staticShader.create();
        staticShader.bind();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.unbind();
    }

    public void prepare() {
        glEnable(GL_DEPTH_TEST);    // test which triangles are in front and render them in the correct order
        glClearColor(.95f, .9f, .67f, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear screen and draw with color in color buffer
    }

    public void render(RawModel rawModel) {
        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // TODO: need an ENUM to define VAO attributes I am using
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
        glDisableVertexAttribArray(0);  // TODO: need an ENUM to define VAO attributes I am using
        glBindVertexArray(0);                                   // Unbind the VAO
    }

    public void render(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        int textureId = texturedModel.getModelTexture().getTextureId();

        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // VAO 0 = vertex spacial coordinates
        glEnableVertexAttribArray(1);   // VAO 1 = texture coordinates
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);    // sampler2D in fragment shader  uses texture bank 0 by default
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
        glDisableVertexAttribArray(0);  // VAO 0 = vertex spacial coordinates
        glDisableVertexAttribArray(1);  // VAO 1 = texture coordinates
        glDisableVertexAttribArray(2);  // VAO 2 = normals
        glBindVertexArray(0);   // Unbind the VAO
    }

    public void render(Entity entity, StaticShader staticShader) {
        TexturedModel texturedModel = entity.getTexturedModel();
        RawModel rawModel = texturedModel.getRawModel();
        int textureId = texturedModel.getModelTexture().getTextureId();

        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // VAO 0 = vertex spacial coordinates
        glEnableVertexAttribArray(1);   // VAO 1 = texture coordinates
        glEnableVertexAttribArray(2);   // VAO 2 = normals

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        staticShader.loadTransformationMatrix(transformationMatrix);

        ModelTexture texture = texturedModel.getModelTexture();
        staticShader.loadSpecularLight(texture.getShineDamper(), texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);    // sampler2D in fragment shader  uses texture bank 0 by default
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
        glDisableVertexAttribArray(0);  // VAO 0 = vertex spacial coordinates
        glDisableVertexAttribArray(1);  // VAO 1 = texture coordinates
        glDisableVertexAttribArray(2);  // VAO 2 = normals
        glBindVertexArray(0);   // Unbind the VAO
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWindowWidth() / (float) DisplayManager.getWindowHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }
}
