package com.chrisgruber.thinmatrixgame.engine.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.system.libffi.FFICIF;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static boolean[] buttons;
    private static double mouseX, mouseY;
    private static double mouseScrollX, mouseScrollY;
    final private GLFWCursorPosCallback mouseMove;
    final private GLFWMouseButtonCallback mouseButtons;
    final private GLFWScrollCallback mouseScroll;

    public Mouse() {
        buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];

        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void close() {
                super.close();
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public FFICIF getCallInterface() {
                return super.getCallInterface();
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void callback(long ret, long args) {
                super.callback(ret, args);
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW_RELEASE);
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public FFICIF getCallInterface() {
                return super.getCallInterface();
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void callback(long ret, long args) {
                super.callback(ret, args);
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void close() {
                super.close();
            }
        };

        mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetX, double offsetY) {
                mouseScrollX += offsetX;
                mouseScrollY += offsetY;
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public FFICIF getCallInterface() {
                return super.getCallInterface();
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void callback(long ret, long args) {
                super.callback(ret, args);
            }

            // TODO: Upgrading to LWJGL 3.3.3 seems to have forced implementing these methods
            @Override
            public void close() {
                super.close();
            }
        };
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }

    public GLFWScrollCallback getMouseScrollCallback() {
        return mouseScroll;
    }

    public void destroy() {
        mouseMove.free();
        mouseButtons.free();
        mouseScroll.free();
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static double getMouseScrollX() {
        return mouseScrollX;
    }

    public static double getMouseScrollY() {
        return mouseScrollY;
    }
}
