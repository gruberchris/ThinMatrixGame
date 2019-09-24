package com.chrisgruber.thinmatrixgame.graphics;

import com.chrisgruber.thinmatrixgame.graphics.utils.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class ModelLoader {
    private final int vertexLength = 3;
    private List<Integer> vaoList;
    private List<Integer> vboList;

    public ModelLoader() {
        vaoList = new ArrayList<Integer>();
        vboList = new ArrayList<Integer>();
    }

    private int createVao() {
        int vaoId = glGenVertexArrays();            // initialize an empty VAO
        vaoList.add(vaoId);
        glBindVertexArray(vaoId);                   // select this vao
        return vaoId;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboId = glGenBuffers();                                 // initialize an empty VBO
        vboList.add(vboId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);                       // select this VBO into the VAO Id specified
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data);   // make VBO from data
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);      // store data into VBO & Not going to edit this data
        glVertexAttribPointer(attributeNumber, vertexLength, GL_FLOAT, false, 0, 0);    // place VBO into VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);                   // unbind the VBO
    }

    private void unbindVao() {
        glBindVertexArray(0);
    }

    public RawModel loadToVao(float[] positions) {
        int vaoId = createVao();
        storeDataInAttributeList(0, positions);     // using VAO attribute 0. Could be any 0 thru 15
        unbindVao();
        return new RawModel(vaoId, positions.length / vertexLength);
    }

    public void Destroy() {
        for (int vaoId : vaoList) {
            glDeleteVertexArrays(vaoId);
        }

        for (int vboId : vboList) {
            glDeleteBuffers(vboId);
        }
    }
}
