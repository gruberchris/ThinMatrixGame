package com.chrisgruber.thinmatrixgame.graphics;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

public class DisplayManager {
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 760;
    private static long window;
    private static String title = "ThinMatrix Game";
    private static int frames;
    private static long time;
    private static boolean showFPSTitle;

    public static void createDisplay() {
        if (!glfwInit()) {
            throw new RuntimeException("ERROR: GLFW wasn't initialized");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, title, 0, 0);

        if (window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        glfwSetWindowPos(window, (vidMode.width() - WINDOW_WIDTH) / 2, (vidMode.height() - WINDOW_HEIGHT) / 2);

        glfwMakeContextCurrent(window);
        createCapabilities();
        glfwShowWindow(window);

        // Setting the value to 1 should limit to 60 FPS
        glfwSwapInterval(1);
    }

    public static void updateDisplay() {
        glfwPollEvents();
        glfwSwapBuffers(window);

        if (showFPSTitle) {
            frames++;

            if (System.currentTimeMillis() > time + 1000) {
                glfwSetWindowTitle(window, title + " | FPS: " + frames);
                time = System.currentTimeMillis();
                frames = 0;
            }
        }
    }

    public static void closeDisplay() {
        glfwWindowShouldClose(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static boolean shouldDisplayClose() {
        return !glfwWindowShouldClose(window);
    }

    public static String getOpenGlVersionMessage() {
        return glGetString(GL_VERSION);
    }

    public static void setShowFPSTitle(boolean showFPSTitle) {
        DisplayManager.showFPSTitle = showFPSTitle;

        if (!showFPSTitle) {
            frames = 0;
            time = 0;
        }
    }
}
