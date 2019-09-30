package com.chrisgruber.thinmatrixgame.engine;

import com.chrisgruber.thinmatrixgame.engine.models.RawModel;
import com.chrisgruber.thinmatrixgame.engine.utils.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class ModelLoader {
    private List<Integer> vaoList;
    private List<Integer> vboList;
    private List<Integer> textureList;

    public ModelLoader() {
        vaoList = new ArrayList<>();
        vboList = new ArrayList<>();
        textureList = new ArrayList<>();
    }

    private int createVao() {
        int vaoId = glGenVertexArrays();            // initialize an empty VAO
        vaoList.add(vaoId);
        glBindVertexArray(vaoId);                   // select this vao
        return vaoId;
    }

    private void storeDataInAttributeList(int attributeNumber, int vertexLength, float[] data) {
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

    private void bindIndicesBuffer(int[] indices) {
        int vboId = glGenBuffers();
        vboList.add(vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public RawModel loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoId = createVao();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);     // using VAO attribute 0. Could be any 0 thru 15
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVao();
        return new RawModel(vaoId, indices.length);
    }

    public int loadTexture(String filename) {
        TextureLoader textureLoader = new TextureLoader(filename);
        int textureId = textureLoader.getTextureId();
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
        textureList.add(textureId);
        return textureId;
    }

    public void destroy() {
        for (int vaoId : vaoList) {
            glDeleteVertexArrays(vaoId);
        }

        for (int vboId : vboList) {
            glDeleteBuffers(vboId);
        }

        for (int textureId : textureList) {
            glDeleteTextures(textureId);
        }
    }
}
