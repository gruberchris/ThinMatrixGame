package com.chrisgruber.thinmatrixgame.graphics;

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
        glDrawArrays(GL_TRIANGLES, 0, rawModel.getVertexCount());   // draw model using triangles
        glDisableVertexAttribArray(0);  // TODO: need an ENUM to define VAO attributes I am using
        glBindVertexArray(0);                                   // Unbind the VAO
    }
}
