package com.chrisgruber.thinmatrixgame.graphics.shaders;

import com.chrisgruber.thinmatrixgame.graphics.utils.FileUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgramBase {
    private String vertexFile, fragmentFile;
    private int programId, vertexShaderId, fragmentShaderId;

    public ShaderProgramBase(String vertexPath, String fragmentPath) {
        vertexFile = FileUtils.loadAsString(vertexPath);
        fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programId, attribute, variableName);
    }

    public void create() {
        programId = glCreateProgram();
        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShaderId, vertexFile);
        glCompileShader(vertexShaderId);

        // Error check shader program code after trying to compile it
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            // The shader program didn't compile
            throw new RuntimeException("Vertex Shader: " + glGetShaderInfoLog(vertexShaderId));
        }

        // The shader code did successfully compile
        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShaderId, fragmentFile);
        glCompileShader(fragmentShaderId);

        // Error check fragment shader program code after attempt to compile it
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            // The fragment shader program didn't compile
            throw new RuntimeException("Fragment Shader: " + glGetShaderInfoLog(fragmentShaderId));
        }

        // The fragment shader code did successfully compile
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        bindAttributes();

        // Linking
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program Linking: " + glGetProgramInfoLog(programId));
        }

        // Validating
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program Validation: " + glGetProgramInfoLog(programId));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }
}
