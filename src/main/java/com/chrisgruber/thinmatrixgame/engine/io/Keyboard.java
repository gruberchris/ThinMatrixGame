package com.chrisgruber.thinmatrixgame.engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.libffi.FFICIF;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback {
    private static final boolean[] keys = new boolean[GLFW_KEY_LAST];

    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW.GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

    // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
    @Override
    public void close() {
        super.close();
    }

    // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
    @Override
    public void callback(long ret, long args) {
        super.callback(ret, args);
    }

    // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
    @Override
    public FFICIF getCallInterface() {
        return super.getCallInterface();
    }
}
