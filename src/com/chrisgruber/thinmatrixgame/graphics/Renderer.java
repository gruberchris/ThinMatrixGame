package com.chrisgruber.thinmatrixgame.graphics;

import com.chrisgruber.thinmatrixgame.graphics.entities.Entity;
import com.chrisgruber.thinmatrixgame.graphics.models.RawModel;
import com.chrisgruber.thinmatrixgame.graphics.models.TexturedModel;
import com.chrisgruber.thinmatrixgame.graphics.shaders.StaticShader;
import com.chrisgruber.thinmatrixgame.graphics.utils.Maths;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public void prepare() {
        glClearColor(1, 0, 0, 1);      // Load selected color into the color buffer
        glClear(GL_COLOR_BUFFER_BIT);                           // Clear screen and draw with color in color buffer
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
        int textureId = texturedModel.getModelTexture().getId();

        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // VAO 0 = vertex spacial coordinates
        glEnableVertexAttribArray(1);   // VAO 1 = texture coordinates
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);    // sampler2D in fragment shader  uses texture bank 0 by default
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
        glDisableVertexAttribArray(0);  // VAO 0 = vertex spacial coordinates
        glDisableVertexAttribArray(1);  // VAO 1 = texture coordinates
        glBindVertexArray(0);   // Unbind the VAO
    }

    public void render(Entity entity, StaticShader staticShader) {
        TexturedModel texturedModel = entity.getTexturedModel();
        RawModel rawModel = texturedModel.getRawModel();
        int textureId = texturedModel.getModelTexture().getId();

        glBindVertexArray(rawModel.getVaoId());
        glEnableVertexAttribArray(0);   // VAO 0 = vertex spacial coordinates
        glEnableVertexAttribArray(1);   // VAO 1 = texture coordinates

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        staticShader.loadTransformationMatrix(transformationMatrix);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);    // sampler2D in fragment shader  uses texture bank 0 by default
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);    // Draw using index buffer and triangles
        glDisableVertexAttribArray(0);  // VAO 0 = vertex spacial coordinates
        glDisableVertexAttribArray(1);  // VAO 1 = texture coordinates
        glBindVertexArray(0);   // Unbind the VAO
    }
}
